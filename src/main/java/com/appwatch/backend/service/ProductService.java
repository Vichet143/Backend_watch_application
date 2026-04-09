package com.appwatch.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.appwatch.backend.model.ProductModel;
import com.appwatch.backend.repository.ProductRepository;

@Service
public class ProductService {
  private ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductModel> getAllProducts() {
    return productRepository.findAll();
  }

  public ProductModel addProduct(ProductModel product) {
    List<String> missingOrInvalidFields = new ArrayList<>();

    if (!hasText(product.getProduct_name())) {
      missingOrInvalidFields.add("product_name");
    }
    if (!hasText(product.getDescription())) {
      missingOrInvalidFields.add("description");
    }
    if (product.getPrice() == null || product.getPrice() <= 0) {
      missingOrInvalidFields.add("price");
    }
    if (product.getQuantity() < 0) {
      missingOrInvalidFields.add("quantity");
    }
    if (!hasText(product.getCategory())) {
      missingOrInvalidFields.add("category");
    }
    if (!hasText(product.getBrand())) {
      missingOrInvalidFields.add("brand");
    }
    if (product.getColors() == null || product.getColors().isEmpty()) {
      missingOrInvalidFields.add("colors");
    }
    if (!hasText(product.getImage_url())) {
      missingOrInvalidFields.add("image_url");
    }

    if (!missingOrInvalidFields.isEmpty()) {
      throw new IllegalArgumentException("Missing or invalid fields: " + String.join(", ", missingOrInvalidFields));
    }

    return productRepository.save(product);
  }

  private boolean hasText(String value) {
    return value != null && !value.trim().isEmpty();
  }

  public ProductModel getProductById(String id) {
    return productRepository.findById(id).orElse(null);
  }

  public ProductModel updateProduct(String id, ProductModel updatedProduct) {
    ProductModel existingProduct = productRepository.findById(id).orElse(null);
    if (existingProduct == null) {
      throw new IllegalArgumentException("Product not found with id: " + id);
    }

    // Update colors if provided
    if (updatedProduct.getColors() != null && !updatedProduct.getColors().isEmpty()) {
      existingProduct.setColors(updatedProduct.getColors());
    }

    // Update other fields if provided
    if (hasText(updatedProduct.getProduct_name())) {
      existingProduct.setProduct_name(updatedProduct.getProduct_name());
    }
    if (hasText(updatedProduct.getDescription())) {
      existingProduct.setDescription(updatedProduct.getDescription());
    }
    if (hasText(updatedProduct.getImage_url())) {
      existingProduct.setImage_url(updatedProduct.getImage_url());
    }
    if (updatedProduct.getPrice() != null && updatedProduct.getPrice() > 0) {
      existingProduct.setPrice(updatedProduct.getPrice());
    }
    if (updatedProduct.getQuantity() >= 0) {
      existingProduct.setQuantity(updatedProduct.getQuantity());
    }
    if (hasText(updatedProduct.getCategory())) {
      existingProduct.setCategory(updatedProduct.getCategory());
    }
    if (hasText(updatedProduct.getBrand())) {
      existingProduct.setBrand(updatedProduct.getBrand());
    }
    if (updatedProduct.getDiscount() != null && updatedProduct.getDiscount() >= 0) {
      existingProduct.setDiscount(updatedProduct.getDiscount());
    }
    if (hasText(updatedProduct.getProduct_type())) {
      existingProduct.setProduct_type(updatedProduct.getProduct_type());
    }
    if (updatedProduct.getWarranty_period() >= 0) {
      existingProduct.setWarranty_period(updatedProduct.getWarranty_period());
    }

    return productRepository.save(existingProduct);
  }
}
