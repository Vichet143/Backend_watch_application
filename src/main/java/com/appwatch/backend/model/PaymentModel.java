package com.appwatch.backend.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "payments")
public class PaymentModel {
  @Id
  private String paymentId;
  private Double amount;
  private String bakong_hash;
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime created_at = LocalDateTime.now();
  private String currency;
  private String description;
  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime paid_at;
  private String payment_method;
  private String qr_code;
  private String qr_expiration;
  private String qr_md5;
  private String qr_string;
  private String status;
  private String transaction_id;
  private int checkoutId;
  private int cartId;

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getBakong_hash() {
    return bakong_hash;
  }

  public void setBakong_hash(String bakong_hash) {
    this.bakong_hash = bakong_hash;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getPaid_at() {
    return paid_at;
  }

  public void setPaid_at(LocalDateTime paid_at) {
    this.paid_at = paid_at;
  }

  public String getPayment_method() {
    return payment_method;
  }

  public void setPayment_method(String payment_method) {
    this.payment_method = payment_method;
  }

  public String getQr_code() {
    return qr_code;
  }

  public void setQr_code(String qr_code) {
    this.qr_code = qr_code;
  }

  public String getQr_expiration() {
    return qr_expiration;
  }

  public void setQr_expiration(String qr_expiration) {
    this.qr_expiration = qr_expiration;
  }

  public String getQr_md5() {
    return qr_md5;
  }

  public void setQr_md5(String qr_md5) {
    this.qr_md5 = qr_md5;
  }

  public String getQr_string() {
    return qr_string;
  }

  public void setQr_string(String qr_string) {
    this.qr_string = qr_string;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTransaction_id() {
    return transaction_id;
  }

  public void setTransaction_id(String transaction_id) {
    this.transaction_id = transaction_id;
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
}
