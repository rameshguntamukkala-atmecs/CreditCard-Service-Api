package com.bank.creditCard.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bank.creditCard.offers.CreditCardOffers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * This is an entity class for table CREDIT_CARD_NAMES
 *
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "credit_card_names")
public class CreditCardName {

	@Id
	private String creditCardId;
	private String creditCardName;
	private String creditCardType;
	private Short validityInMonths;
	private String cardDescription;
	
	@Transient
	CreditCardOffers cardOffers;

		
}
