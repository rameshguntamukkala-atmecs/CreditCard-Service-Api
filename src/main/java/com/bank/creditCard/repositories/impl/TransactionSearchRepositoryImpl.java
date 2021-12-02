package com.bank.creditCard.repositories.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.io.entities.TransactionSearchQuery;
import com.bank.creditCard.repositories.TransactionSearchRepository;

@Repository
public class TransactionSearchRepositoryImpl implements TransactionSearchRepository{
	
	@Autowired
	NamedParameterJdbcTemplate template;
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionSearchRepositoryImpl.class);
	
	private String SELECT_TRANSACTIONS = "SELECT * FROM CREDIT_CARD_TRANSACTION_DETAILS T WHERE T.CARD_NUMBER = :cardNo :WHERE";
	
	@Override
	public List<TransactionDetails> getTransactionDetails(TransactionSearchQuery searchQuery) {
		
		
		Map<String, String> params = new HashMap<>();
		
		params.put("cardNo", searchQuery.getCardNumber().toString());
		
		String whereClause = constractWhereClause(searchQuery, params);
		
		String sql = SELECT_TRANSACTIONS.replace(":WHERE", whereClause);
		
		logger.info("Transaction Search Query: {}", sql);
		
		List<TransactionDetails> transactionList = null;
		try {
			transactionList = template.query(sql, params, BeanPropertyRowMapper.newInstance(TransactionDetails.class));
		} catch (DataAccessException e) {
			logger.info("Query failed", e);
		}
		
		return transactionList;
	}

	private String constractWhereClause(TransactionSearchQuery searchQuery, Map<String, String> params) {
		
		StringBuilder whereClause = new StringBuilder();
		
		if(searchQuery.getTransactionType() !=  null) {
			whereClause.append("AND T.TRANSACTION_TYPE = :transactionType ") ;
			params.put("transactionType", searchQuery.getTransactionType().toString());
		} 
		
		if(searchQuery.getTransactionDate() != null) {
			whereClause.append("AND CONVERT(DATE, T.TRANSACTION_TIME) = :transactionDate ");
			params.put("transactionDate", searchQuery.getTransactionDate().toString());
			
		} else if ( searchQuery.getFromDate()  != null 
				&& searchQuery.getToDate() != null) {
			
			whereClause.append("AND CONVERT(DATE, T.TRANSACTION_TIME) BETWEEN :fromDate AND :toDate " );
			params.put("fromDate", searchQuery.getFromDate().toString());
			params.put("toDate", searchQuery.getToDate().toString());
		}
		
		
		whereClause.append( " ORDER BY T.TRANSACTION_TIME ASC ");
		
		return whereClause.toString();
	}

}
