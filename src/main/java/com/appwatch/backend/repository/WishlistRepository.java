package com.appwatch.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.appwatch.backend.model.WishlistModel;

public interface WishlistRepository extends MongoRepository<WishlistModel, String> {
  List<WishlistModel> findByUserId(String userId);

  Optional<WishlistModel> findByUserIdAndProductId(String userId, String productId);

  long deleteByUserIdAndProductId(String userId, String productId);
}
