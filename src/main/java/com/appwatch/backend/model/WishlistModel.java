package com.appwatch.backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "wishlists")
public class WishlistModel {

  @Id
  private String wishlist_id;

  private String userId;
  private String productId;
  private String product_name;

  private String product_image_url;

  private Double product_price;

  private String product_description;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
  private LocalDateTime create_at = LocalDateTime.now();

  public WishlistModel(String userId, String productId) {
    this.userId = userId;
    this.productId = productId;
  }

  public String getWishlist_id() {
    return wishlist_id;
  }

  public void setWishlist_id(String wishlist_id) {
    this.wishlist_id = wishlist_id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public LocalDateTime getCreate_at() {
    return create_at;
  }

  public void setCreate_at(LocalDateTime create_at) {
    this.create_at = create_at;
  }

  public String getProduct_name() {
    return product_name;
  }

  public void setProduct_name(String product_name) {
    this.product_name = product_name;
  }

  public String getProduct_image_url() {
    return product_image_url;
  }

  public void setProduct_image_url(String product_image_url) {
    this.product_image_url = product_image_url;
  }

  public Double getProduct_price() {
    return product_price;
  }

  public void setProduct_price(Double product_price) {
    this.product_price = product_price;
  }

  public String getProduct_description() {
    return product_description;
  }

  public void setProduct_description(String product_description) {
    this.product_description = product_description;
  }

}
