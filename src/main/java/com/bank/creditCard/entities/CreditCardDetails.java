package com.bank.creditCard.entities;

import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name= "USER_CREDIT_CARD_DETAILS")
public class CreditCardDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name =  "USER_CARD_ID")
	private Long userCardId;
	
	@Column (name = "USER_ID")
	private Long userId;
	
	@Column (name = "CARD_NAME")
	private String cardName;
	
	@Column (name = "CARD_TYPE")
	private String cardType;
	
	@Column (name = "CARD_NUMBER")
	private Long cardNumber;
	
	@Column (name = "VALID_FROM")
	private Date validFrom;
	
	@Column (name = "VALID_TO")
	private Date validTo;
	
	@Column (name = "CARD_VERIFICATION_VALUE")
	private String cardVerificatonValue;
	
	@Column (name = "CARD_PIN")
	private String cardPin;
	
	@Column (name = "NAME_ON_CARD")
	private String nameOnCard;
	
	@Column (name = "CARD_LIMIT")
	private BigDecimal cardLimit;
	
	@Column (name = "CARD_STATUS")
	private Short cardStatus;
	
	@Column (name = "CREATED_TIME")
	private Timestamp createdTime;
	
	@Column (name = "MODIFIED_TIME")
	private Timestamp modifiedTime;
	
	@Column (name = "OUT_STANDING_AMOUNT")
	private BigDecimal outStandingAmount;
	
	@Column (name = "OUT_STANDING_AMOUNT_MODIFIED_TIME")
	private Timestamp outStandingAmountModifiedTime;
	
}
