package com.appwatch.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appwatch.backend.dto.ApiResponse;
import com.appwatch.backend.dto.CartRequest;
import com.appwatch.backend.model.CartModel;
import com.appwatch.backend.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse<CartModel>> addToCart(
      @RequestHeader("Authorization") String authorization,
      @RequestBody CartRequest request) {
    CartModel cartItem = cartService.addToCart(authorization, request.getProductId());
    ApiResponse<CartModel> response = new ApiResponse<>(true, "Cart item added successfully.", cartItem);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
