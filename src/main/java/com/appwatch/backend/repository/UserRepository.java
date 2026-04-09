package com.appwatch.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.appwatch.backend.model.User;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // You can add custom queries here if needed
    Optional<User> findByEmail(String email);
}
