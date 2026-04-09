package com.appwatch.backend.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.appwatch.backend.model.ProductModel;

public interface ProductRepository extends MongoRepository<ProductModel, String> {
    // You can add custom queries here if needed
}
