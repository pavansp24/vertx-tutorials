package com.vertx.springboot.integration.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.vertx.springboot.integration.jpa.model.PaymentTransaction;
import com.vertx.springboot.integration.spring.repo.PaymentTransactionRepo;


/**
 * 
 * @author pavan
 *
 */
@Component
public class CrudImpl {

	@Autowired
	private PaymentTransactionRepo repo;

	@Transactional
	public void save(final PaymentTransaction transaction) {
		repo.save(transaction);
	}
	
	public PaymentTransaction getByTRansactionId(String transactionId) {
		return repo.findByTxnId(transactionId);
	}
	
	public List<PaymentTransaction> getTransactions(int page) {
		Pageable pageRequest = PageRequest.of(page, 10);
		return repo.findAll(pageRequest).getContent();
	}

	public PaymentTransactionRepo getRepo() {
		return repo;
	}

	public void setRepo(PaymentTransactionRepo repo) {
		this.repo = repo;
	}
}
