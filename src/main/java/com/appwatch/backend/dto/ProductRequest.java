package com.appwatch.backend.dto;

public class ProductRequest {
  private String product_id;

  public ProductRequest(String product_id) {
    this.product_id = product_id;
  }

  public String getProduct_id() {
    return product_id;
  }
  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }
}
