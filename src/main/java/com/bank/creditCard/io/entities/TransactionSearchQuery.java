package com.bank.creditCard.io.entities;

import java.sql.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TransactionSearchQuery {

	@NotNull
	private Long cardNumber;
	private Short transactionType;
	private Date transactionDate;
	private Date fromDate;
	private Date toDate;
	
}
