package com.vertx.springboot.integration.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vertx.springboot.integration.jpa.model.PaymentTransaction;

@Repository
public interface PaymentTransactionRepo extends JpaRepository<PaymentTransaction, Integer> {

	@Query(value = "select p from PaymentTransaction p where p.transactionId=?1")
	public PaymentTransaction findByTxnId(String transactionId);

}
