package com.appwatch.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.appwatch.backend.model.ProductModel;
import com.appwatch.backend.model.User;
import com.appwatch.backend.model.WishlistModel;
import com.appwatch.backend.repository.ProductRepository;
import com.appwatch.backend.repository.UserRepository;
import com.appwatch.backend.repository.WishlistRepository;
import com.appwatch.backend.util.JwtUtil;

@Service
public class WishlistService {

  private final WishlistRepository wishlistRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository,
      UserRepository userRepository, JwtUtil jwtUtil) {
    this.wishlistRepository = wishlistRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  public WishlistModel addToWishlistByToken(String authorizationHeader, String productId) {
    if (!hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Authorization token is required.");
    }
    if (!hasText(productId)) {
      throw new IllegalArgumentException("productId is required.");
    }

    String token = authorizationHeader.substring(7).trim();
    String email = jwtUtil.extractEmail(token);
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
    String userId = user.getId();

    if (wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
      throw new IllegalArgumentException("Product already exists in wishlist.");
    }

    ProductModel product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("Product not found."));

    WishlistModel wishlist = new WishlistModel(userId, productId);
    wishlist.setProduct_name(product.getProduct_name());
    wishlist.setProduct_description(product.getDescription());
    wishlist.setProduct_image_url(product.getImage_url());
    wishlist.setProduct_price(product.getPrice());

    return wishlistRepository.save(wishlist);
  }

  public List<WishlistModel> getWishlistByUserId(String userId) {
    if (!hasText(userId)) {
      throw new IllegalArgumentException("userId is required.");
    }
    return wishlistRepository.findByUserId(userId);
  }

  public WishlistModel removeFromWishlist(String userId, String productId) {
    if (!hasText(userId) || !hasText(productId)) {
      throw new IllegalArgumentException("userId and productId are required.");
    }

    WishlistModel existing = wishlistRepository.findByUserIdAndProductId(userId, productId)
        .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found."));

    wishlistRepository.delete(existing);
    return existing;
  }

  private boolean hasText(String value) {
    return value != null && !value.trim().isEmpty();
  }
}
