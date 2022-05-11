package com.bank.creditCard.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.io.entities.TransactionSearchQuery;

public interface TransactionSearchRepository {
	public List<TransactionDetails> getTransactionDetails(TransactionSearchQuery searchQuery);
}
