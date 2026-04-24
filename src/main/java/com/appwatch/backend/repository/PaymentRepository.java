package com.appwatch.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.appwatch.backend.model.PaymentModel;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends MongoRepository<PaymentModel, String> {
  @Query("{ 'transaction_id': ?0 }")
  Optional<PaymentModel> findPaymentByTransactionId(String transactionId);


  List<PaymentModel> findByStatus(String status);

}
