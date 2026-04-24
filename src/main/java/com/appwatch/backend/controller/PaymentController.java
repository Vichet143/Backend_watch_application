package com.appwatch.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appwatch.backend.dto.ApiResponse;
import com.appwatch.backend.dto.PaymentRequest;
import com.appwatch.backend.dto.PaymentRespone;
import com.appwatch.backend.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/generate")
  public ResponseEntity<ApiResponse<PaymentRespone>> generate(@RequestBody PaymentRequest request) {
    PaymentRespone payment = paymentService.generatePayment(request);
    ApiResponse<PaymentRespone> response = new ApiResponse<>(true, "Payment generated successfully.", payment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

}
