package com.appwatch.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appwatch.backend.dto.ApiResponse;
import com.appwatch.backend.dto.ProductRequest;
import com.appwatch.backend.model.ProductModel;
import com.appwatch.backend.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @PostMapping("/create")
  public ResponseEntity<ApiResponse<ProductModel>> createProduct(@RequestBody ProductModel product) {
    ProductModel savedProduct = service.addProduct(product);
    ApiResponse<ProductModel> response = new ApiResponse<>(true, "Product created successfully.", savedProduct);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("")
  public ResponseEntity<ApiResponse<Iterable<ProductModel>>> getAllProducts() {
    Iterable<ProductModel> products = service.getAllProducts();
    ApiResponse<Iterable<ProductModel>> response = new ApiResponse<>(true, "Products retrieved successfully.",
        products);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/getById")
  public ResponseEntity<ApiResponse<ProductModel>> getProductById(@RequestBody ProductRequest request) {
    String id = request.getProduct_id();
    ProductModel product = service.getProductById(id);
    if (product != null) {
      ApiResponse<ProductModel> response = new ApiResponse<>(true, "Product retrieved successfully.", product);
      return ResponseEntity.ok(response);
    } else {
      ApiResponse<ProductModel> response = new ApiResponse<>(false, "Product not found.", null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
  }

  @PutMapping("/update")
  public ResponseEntity<ApiResponse<ProductModel>> updateProduct(@RequestParam String id,
      @RequestBody ProductModel updatedProduct) {
    try {
      ProductModel product = service.updateProduct(id, updatedProduct);
      ApiResponse<ProductModel> response = new ApiResponse<>(true, "Product updated successfully.", product);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      ApiResponse<ProductModel> response = new ApiResponse<>(false, e.getMessage(), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
  }
}
