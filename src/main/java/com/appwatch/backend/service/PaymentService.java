package com.appwatch.backend.service;

import com.appwatch.backend.dto.PaymentRequest;
import com.appwatch.backend.dto.PaymentRespone;
import com.appwatch.backend.model.PaymentModel;
import com.appwatch.backend.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;

  @Value("${bakong.account-id:}")
  private String defaultBakongAccountId;

  @Value("${bakong.account-name:}")
  private String defaultMerchantName;

  @Value("${bakong.token:}")
  private String bakongToken;

  @Value("${bakong.api-base:https://api-bakong.nbc.gov.kh/v1}")
  private String bakongApiBase;

  public PaymentService(PaymentRepository paymentRepository, ObjectMapper objectMapper) {
    this.paymentRepository = paymentRepository;
    this.objectMapper = objectMapper;
    this.httpClient = HttpClient.newHttpClient();
  }

  public PaymentRespone generatePayment(PaymentRequest paymentRequest) {
    if (paymentRequest == null) {
      throw new IllegalArgumentException("Payment request cannot be null");
    }
    if (paymentRequest.getAmount() == null || paymentRequest.getAmount() <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }

    if (paymentRequest.getCheckoutId() <= 0 ) {
      throw new IllegalArgumentException("checkoutId or deliveryId is required.");
    }
    String receiverAccount = hasText(paymentRequest.getBakongAccountId())
        ? paymentRequest.getBakongAccountId().trim()
        : safeTrim(defaultBakongAccountId);
    String merchantName = hasText(paymentRequest.getMerchantName())
        ? paymentRequest.getMerchantName().trim()
        : safeTrim(defaultMerchantName);

    if (!hasText(merchantName)) {
      throw new IllegalArgumentException("Merchant name cannot be empty");
    }

    if (!hasText(receiverAccount)) {
      throw new IllegalArgumentException("Bakong account ID cannot be empty");
    }

    if (hasText(paymentRequest.getCurrency()) && !"USD".equalsIgnoreCase(paymentRequest.getCurrency())) {
      throw new IllegalArgumentException("Only USD is supported.");
    }

    IndividualInfo individualInfo = new IndividualInfo();
    individualInfo.setBakongAccountId(receiverAccount);
    individualInfo.setAccountInformation("Payment for order #" + paymentRequest.getCheckoutId());
    individualInfo.setAcquiringBank("Dev Bank");
    individualInfo.setCurrency(KHQRCurrency.USD);
    individualInfo.setAmount(paymentRequest.getAmount());
    individualInfo.setMerchantName(merchantName);
    individualInfo.setMerchantCity("PHNOM PENH");
    individualInfo.setStoreLabel("Watch Store");
    // Bakong expects dynamic expiration timestamp in epoch milliseconds (13
    // digits).
    long expirationEpochMillis = System.currentTimeMillis() + (15 * 60 * 1000L);
    individualInfo.setExpirationTimestamp(expirationEpochMillis);

    KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(individualInfo);
    int statusCode = response == null || response.getKHQRStatus() == null ? -1 : response.getKHQRStatus().getCode();
    if (statusCode != 0 || response == null || response.getData() == null) {
      String sdkStatus = response == null || response.getKHQRStatus() == null
          ? "unknown"
          : response.getKHQRStatus().toString();
      throw new IllegalArgumentException(
          "Failed to generate KHQR payload. code=" + statusCode + ", status=" + sdkStatus);
    }

    PaymentModel paymentModel = new PaymentModel();
    paymentModel.setAmount(paymentRequest.getAmount());
    paymentModel.setCheckoutId(paymentRequest.getCheckoutId());
    paymentModel.setCartId(paymentRequest.getCartId());
    paymentModel.setCurrency("USD");
    paymentModel.setStatus("pending");
    paymentModel.setBakong_hash("");
    paymentModel.setQr_expiration(String.valueOf(expirationEpochMillis));
    paymentModel.setQr_md5(response.getData().getMd5());
    paymentModel.setQr_string(response.getData().getQr());
    paymentModel.setTransaction_id(UUID.randomUUID().toString());
    paymentModel.setPayment_method("bakong_khqr");
    paymentModel.setPaid_at(null);
    paymentModel.setCreated_at(java.time.LocalDateTime.now());
    paymentModel.setQr_code(buildQrImageDataUri(response.getData().getQr()));
    PaymentModel saved = paymentRepository.save(paymentModel);
    saveGeneratedQrImage(response.getData().getQr(), saved.getPaymentId());

    PaymentRespone responseBody = mapToResponse(saved);
    return responseBody;

  }

  @Scheduled(fixedDelayString = "${payment.poll.interval-ms:5000}")
  public void autoSyncPendingPayments() {
    if (!hasText(bakongToken)) {
      return;
    }

    List<PaymentModel> pendingPayments = paymentRepository.findByStatus("pending");
    for (PaymentModel payment : pendingPayments) {
      try {
        syncPaymentStatus(payment);
      } catch (Exception ex) {
        System.err.println("Failed to auto-sync payment " + payment.getPaymentId() + ": " + ex.getMessage());
      }
    }
  }


  private PaymentRespone mapToResponse(PaymentModel payment) {
    PaymentRespone response = new PaymentRespone();
    response.setPaymentId(payment.getPaymentId());
    response.setAmount(payment.getAmount());
    response.setPaymentStatus(payment.getStatus());
    response.setCheckoutId(payment.getCheckoutId());
    response.setCartId(payment.getCartId());
    response.setCurrency(payment.getCurrency());
    response.setTransactionId(payment.getTransaction_id());
    response.setBakongHash(payment.getBakong_hash());
    response.setQrCode(payment.getQr_code());
    response.setMd5(payment.getQr_md5());
    response.setQrExpiration(payment.getQr_expiration());
    response.setPaidAt(payment.getPaid_at() == null ? null : payment.getPaid_at().toString());
    return response;

  }

  private boolean hasText(String value) {
    return value != null && !value.trim().isEmpty();
  }

  private String safeTrim(String value) {
    return value == null ? "" : value.trim();
  }

  private boolean isExpired(String expirationTimestamp) {
    try {
      long value = Long.parseLong(expirationTimestamp);
      // Accept both epoch seconds (10 digits) and epoch milliseconds (13 digits).
      long expirationMillis = value < 1_000_000_000_000L ? value * 1000L : value;
      return System.currentTimeMillis() > expirationMillis;
    } catch (Exception ex) {
      try {
        return Instant.now().isAfter(Instant.parse(expirationTimestamp));
      } catch (Exception ignored) {
        return false;
      }
    }
  }

  private JsonNode checkTransactionByMd5(String md5) {
    if (!hasText(bakongToken)) {
      throw new IllegalArgumentException("BAKONG_TOKEN is not configured.");
    }
    if (!hasText(md5)) {
      throw new IllegalArgumentException("Payment md5 is missing.");
    }
    try {
      String requestBody = objectMapper.createObjectNode().put("md5", md5).toString();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(bakongApiBase + "/check_transaction_by_md5"))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + bakongToken)
          .POST(HttpRequest.BodyPublishers.ofString(requestBody))
          .build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() < 200 || response.statusCode() >= 300) {
        throw new IllegalArgumentException(
            "Bakong API error: " + response.statusCode() + " " + response.body());
      }
      return objectMapper.readTree(response.body());
    } catch (IllegalArgumentException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new IllegalArgumentException("Failed to verify payment with Bakong API: " + ex.getMessage());
    }
  }

  private PaymentModel syncPaymentStatus(PaymentModel payment) {
    if (payment == null) {
      throw new IllegalArgumentException("Payment cannot be null.");
    }

    if ("paid".equalsIgnoreCase(payment.getStatus()) ||
        "failed".equalsIgnoreCase(payment.getStatus()) ||
        "expired".equalsIgnoreCase(payment.getStatus())) {
      return payment;
    }

    if (isExpired(payment.getQr_expiration())) {
      payment.setStatus("expired");
      return paymentRepository.save(payment);
    }

    JsonNode result = checkTransactionByMd5(payment.getQr_md5());
    int responseCode = result.path("responseCode").asInt(-1);
    JsonNode dataNode = result.path("data");
    if (responseCode == 0 && !dataNode.isMissingNode() && !dataNode.isNull()) {
      payment.setStatus("paid");
      payment.setBakong_hash(dataNode.path("hash").asText(""));
      long acknowledgedDateMs = dataNode.path("acknowledgedDateMs").asLong(0L);
      if (acknowledgedDateMs > 0) {
        payment.setPaid_at(LocalDateTime.ofEpochSecond(acknowledgedDateMs / 1000, 0, ZoneOffset.UTC));
      } else {
        payment.setPaid_at(LocalDateTime.now());
      }
      return paymentRepository.save(payment);
    }

    return payment;
  }

  private String buildQrImageDataUri(String qrText) {
    try {
      byte[] pngBytes = buildQrPngBytes(qrText);
      String base64 = Base64.getEncoder().encodeToString(pngBytes);
      return "data:image/png;base64," + base64;
    } catch (Exception ex) {
      throw new IllegalArgumentException("Failed to build QR image: " + ex.getMessage());
    }
  }

  private byte[] buildQrPngBytes(String qrText) {
    try {
      QRCodeWriter writer = new QRCodeWriter();
      BitMatrix matrix = writer.encode(qrText, BarcodeFormat.QR_CODE, 400, 400);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
      return outputStream.toByteArray();
    } catch (Exception ex) {
      throw new IllegalArgumentException("Failed to build QR image: " + ex.getMessage());
    }
  }

  private void saveGeneratedQrImage(String qrText, String paymentId) {
    try {
      byte[] pngBytes = buildQrPngBytes(qrText);

      Path srcStaticDir = Paths.get("src", "main", "resources", "static");
      Path targetStaticDir = Paths.get("target", "classes", "static");

      writePng(srcStaticDir.resolve("generated-payment-qr.png"), pngBytes);
      writePng(targetStaticDir.resolve("generated-payment-qr.png"), pngBytes);
    } catch (Exception ex) {
      System.err.println("Failed to save generated QR image file: " + ex.getMessage());
    }
  }

  private void writePng(Path path, byte[] data) throws Exception {
    Files.createDirectories(path.getParent());
    Files.write(path, data);
  }

  private String safeFileName(String value) {
    if (!hasText(value)) {
      return "unknown";
    }
    return value.replaceAll("[^a-zA-Z0-9-_]", "_");
  }
}
