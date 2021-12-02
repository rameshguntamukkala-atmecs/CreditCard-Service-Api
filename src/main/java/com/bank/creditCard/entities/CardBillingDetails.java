package com.bank.creditCard.entities;

import java.math.BigDecimal;
import java.sql.Date;
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
@Table (name = "CREDIT_CARD_BILLING_DETAILS")
public class CardBillingDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long billingId;
	
	@Column(name = "CARD_ID")
	private Long cardId;
	
	@Column(name = "BILLING_DATE")
	private Date billingDate;
	
	@Column(name = "PAYMENT_DUE_DATE")
	private Date paymentDueDate;
	
	@Column(name = "NEXT_BILLING_DATE")
	private Date nextBillingDate;
	
	@Column(name = "BILLING_AMOUNT")
	private BigDecimal billingAmount;
	
	@Column (name = "MIN_PAYMENT_AMOUNT")
	private BigDecimal minPaymentAmount;
	
	@Column (name = "PAYMENT_STATUS")
	private Short paymentStatus;
	
	@Column (name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column (name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column (name = "TOTAL_AMOUNT_PAID")
	private BigDecimal totalAmountPaid;
	
}
