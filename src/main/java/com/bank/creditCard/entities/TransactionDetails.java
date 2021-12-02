package com.bank.creditCard.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table (name = "CREDIT_CARD_TRANSACTION_DETAILS")
public class TransactionDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;
	
	@Column(name = "CARD_ID")
	private Long cardId;
	
	@Column(name ="CARD_NUMBER")
	private Long cardNumber;
	
	@Column (name = "AMOUNT")
	private BigDecimal amount;
	
	@Column (name = "TRANSACTION_TYPE")
	private Short transactionType;
	
	@Column (name = "TRANSACTION_STATUS")
	private Short transactionStatus;
	
	@Column (name = "TRANSACTION_MSG")
	private String transactionStatusMsg;
	
	@Column (name = "TRANSACTION_CATEGORY")
	private Short transactionCategory;
	
	@Column (name = "MARCHANT_NAME")
	private String marchantName;
	
	@Column (name = "TRANSACTOIN_PLACE")
	private String transactionPlace;
	
	@Column (name = "TRANSACTION_TIME")
	private Timestamp transactionTime;
	
	@Column (name = "CREATED_TIME")
	private Timestamp createdTime;
	
	@Column (name = "MODIFIED_TIME")
	private Timestamp modifiedTime;
	
	@Column (name = "REWARD_POINT_STATUS")
	private Short rewardPointsStatus;
}
