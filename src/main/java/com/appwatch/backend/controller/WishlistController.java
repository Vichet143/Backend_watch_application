package com.appwatch.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appwatch.backend.dto.ApiResponse;
import com.appwatch.backend.dto.WishlistRequest;
import com.appwatch.backend.model.WishlistModel;
import com.appwatch.backend.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

  private final WishlistService wishlistService;

  public WishlistController(WishlistService wishlistService) {
    this.wishlistService = wishlistService;
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse<WishlistModel>> addToWishlist(
      @RequestHeader("Authorization") String authorization,
      @RequestBody WishlistRequest request) {
    WishlistModel wishlist = wishlistService.addToWishlistByToken(authorization, request.getProductId());
    ApiResponse<WishlistModel> response = new ApiResponse<>(true, "Wishlist item created successfully.", wishlist);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("")
  public ResponseEntity<ApiResponse<List<WishlistModel>>> getWishlistByUser(@RequestBody WishlistRequest request) {
    List<WishlistModel> items = wishlistService.getWishlistByUserId(request.getUserId());
    ApiResponse<List<WishlistModel>> response = new ApiResponse<>(true, "Wishlist retrieved successfully.", items);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/remove")
  public ResponseEntity<ApiResponse<WishlistModel>> removeFromWishlist(@RequestBody WishlistRequest request) {
    WishlistModel removed = wishlistService.removeFromWishlist(request.getUserId(), request.getProductId());
    ApiResponse<WishlistModel> response = new ApiResponse<>(true, "Wishlist item removed successfully.", removed);
    return ResponseEntity.ok(response);
  }
}
