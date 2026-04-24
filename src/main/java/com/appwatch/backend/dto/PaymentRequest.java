package com.appwatch.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class PaymentRequest {
  private int checkoutId;
  private int cartId;
  @JsonAlias("bakong_account_id")
  private String bakongAccountId;
  @JsonAlias("merchant_name")
  private String merchantName;
  private String currency;
  private Double amount;

  public PaymentRequest() {
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

  public String getBakongAccountId() {
    return bakongAccountId;
  }

  public void setBakongAccountId(String bakongAccountId) {
    this.bakongAccountId = bakongAccountId;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }


  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }
}
