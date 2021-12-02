package com.bank.creditCard.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bank.creditCard.offers.CreditCardOffers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "CREDIT_CARD_NAMES")
public class CreditCardName {

	@Id
	@Column(name = "CREDIT_CARD_ID")
	private String creditCardId;
	
	@Column(name = "CREDIT_CARD_NAME")
	private String cardName;
	
	@Column(name = "CREDIT_CARD_TYPE")
	private String creditCardType;

	@Column(name = "CREDIT_CARD_VALIDITY_IN_MONTHS")
	private Short creditCardValidityInMonths;
	
	@Column(name = "CREDIT_CARD_DESCRIPTION")
	private String creditCardDescription;
	
	@Transient
	CreditCardOffers cardOffers;

		
}
