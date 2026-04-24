package com.appwatch.backend.dto;

public class PaymentRespone {
  private String paymentId;
  private String productId;
  private Double amount;
  private String paymentStatus;
  private int checkoutId;
  private int cartId;
  private String currency;
  private String transactionId;
  private String bakongHash;
  private String qrCode;
  private String md5;
  private String qrExpiration;
  private String paidAt;

  public void setQrCode(String qrCode) {
    this.qrCode = qrCode;
  }

  public String getQrCode() {
    return qrCode;
  }

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public int getCheckoutId() {
    return checkoutId;
  }

  public void setCheckoutId(int checkoutId) {
    this.checkoutId = checkoutId;
  }

  public int getCartId() {
    return cartId;
  }

  public void setCartId(int cartId) {
    this.cartId = cartId;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getBakongHash() {
    return bakongHash;
  }

  public void setBakongHash(String bakongHash) {
    this.bakongHash = bakongHash;
  }

  public String getPaidAt() {
    return paidAt;
  }

  public void setPaidAt(String paidAt) {
    this.paidAt = paidAt;
  }

  public String getQrExpiration() {
    return qrExpiration;
  }

  public void setQrExpiration(String qrExpiration) {
    this.qrExpiration = qrExpiration;
  }


  public String showPaymentDetails() {
    return "Payment ID: " + paymentId + "\n" +
        "Product ID: " + productId + "\n" +
        "Amount: " + amount + "\n" +
        "Payment Status: " + paymentStatus + "\n" +
        "Checkout ID: " + checkoutId + "\n" +
        "Cart ID: " + cartId + "\n" +
        "Currency: " + currency + "\n" +
        "Transaction ID: " + transactionId + "\n" +
        "Bakong Hash: " + bakongHash + "\n" +
        "QR Code: " + qrCode + "\n" +
        "MD5 Hash: " + md5 + "\n" +
        "QR Expiration: " + qrExpiration + "\n" +
        "Paid At: " + paidAt;
  }
}
