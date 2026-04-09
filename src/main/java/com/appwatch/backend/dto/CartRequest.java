package com.appwatch.backend.dto;

public class CartRequest {
  
  private String productId;
  private String userId;

  public CartRequest() {
  }

  public CartRequest(String productId, String userId) {
    this.productId = productId;
    this.userId = userId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
