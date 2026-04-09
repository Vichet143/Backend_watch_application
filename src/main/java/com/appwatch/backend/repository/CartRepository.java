package com.appwatch.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.appwatch.backend.model.CartModel;

public interface CartRepository extends MongoRepository<CartModel, String> {

}
