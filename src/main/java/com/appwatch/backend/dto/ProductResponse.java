package com.appwatch.backend.dto;

public class ProductResponse {
  
  private String product_id;
  private String product_name;
  private String product_description;
  private double product_price;
  private String image_url;
  private int quantity;
  private String category;
  private String brand;
  private String color;
  private double discount;
  private String product_type;
  private double warranty_period;
  private long create_at;
  private long update_quantity_at;

  public ProductResponse(String product_id, String product_name, String product_description, double product_price,
      String image_url, int quantity, String category, String brand, String color, double discount, String product_type,
      double warranty_period, long create_at, long update_quantity_at) {
    this.product_id = product_id;
    this.product_name = product_name;
    this.product_description = product_description;
    this.product_price = product_price;
    this.image_url = image_url;
    this.quantity = quantity;
    this.category = category;
    this.brand = brand;
    this.color = color;
    this.discount = discount;
    this.product_type = product_type;
    this.warranty_period = warranty_period;
    this.create_at = create_at;
    this.update_quantity_at = update_quantity_at;
  }
  
  // Getters and Setters
  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }
  
  public String getProduct_name() {
    return product_name;
  }

  public void setProduct_name(String product_name) {
    this.product_name = product_name;
  }

  public String getProduct_description() {
    return product_description;
  }

  public void setProduct_description(String product_description) {
    this.product_description = product_description;
  }

  public double getProduct_price() {
    return product_price;
  }

  public void setProduct_price(double product_price) {
    this.product_price = product_price;
  }

  public String getImage_url() {
    return image_url;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public double getDiscount() {
    return discount;
  }

  public void setDiscount(double discount) {
    this.discount = discount;
  }

  public String getProduct_type() {
    return product_type;
  }

  public void setProduct_type(String product_type) {
    this.product_type = product_type;
  }

  public double getWarranty_period() {
    return warranty_period;
  }

  public void setWarranty_period(double warranty_period) {
    this.warranty_period = warranty_period;
  }

  public long getCreate_at() {
    return create_at;
  }

  public void setCreate_at(long create_at) {
    this.create_at = create_at;
  }

  public long getUpdate_quantity_at() {
    return update_quantity_at;
  }

  public void setUpdate_quantity_at(long update_quantity_at) {
    this.update_quantity_at = update_quantity_at;
  }

  public String showall() {
    return "ProductResponse{product_id='" + product_id + "', product_name='" + product_name + "', product_description='" + product_description + "', product_price=" + product_price + ", image_url='" + image_url + "', quantity=" + quantity + ", category='" + category + "', brand='" + brand + "', color='" + color + "', discount=" + discount + ", product_type='" + product_type + "', warranty_period=" + warranty_period + ", create_at=" + create_at + ", update_quantity_at=" + update_quantity_at + "}";
  }
}
