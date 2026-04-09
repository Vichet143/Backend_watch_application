package com.appwatch.backend.service;

import org.springframework.stereotype.Service;

import com.appwatch.backend.model.CartModel;
import com.appwatch.backend.model.ProductModel;
import com.appwatch.backend.model.User;
import com.appwatch.backend.repository.CartRepository;
import com.appwatch.backend.repository.ProductRepository;
import com.appwatch.backend.repository.UserRepository;
import com.appwatch.backend.util.JwtUtil;

@Service
public class CartService {
  
  private CartRepository cartRepository;
  private ProductRepository productRepository;
  private UserRepository userRepository;
  private JwtUtil jwtUtil;
  
  public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, JwtUtil jwtUtil) {
    this.cartRepository = cartRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;

  }

  public CartModel addToCart(String authorizationHeader, String productId) {
    if (!hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
      throw new IllegalArgumentException("Authorization token is required.");
    }
    if (!hasText(productId)) {
      throw new IllegalArgumentException("Product ID is required.");
    }

    String token = authorizationHeader.substring(7).trim();
    String email = jwtUtil.extractEmail(token);
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
    String userId = user.getId();

    ProductModel product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("Product not found."));

    // Logic to add product to user's cart
    if (!hasText(userId) || !hasText(productId)) {
      throw new IllegalArgumentException("User ID and Product ID cannot be null or empty");
    }
    CartModel cartItem = new CartModel(userId, productId);
    cartItem.setProduct_name(product.getProduct_name());
    cartItem.setProduct_description(product.getDescription());
    cartItem.setProduct_image_url(product.getImage_url());
    cartItem.setProduct_price(product.getPrice());
    cartItem.setQuantity(product.getQuantity()); // Default quantity to 1, can be updated later
    return cartRepository.save(cartItem);
  }
  
  private boolean hasText(String value) {
    return value != null && !value.trim().isEmpty();
  }
}
