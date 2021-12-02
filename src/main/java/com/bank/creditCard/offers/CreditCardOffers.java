package com.bank.creditCard.offers;

import org.springframework.stereotype.Component;

import com.bank.creditCard.factory.BusinessGoldCreditCard;
import com.bank.creditCard.factory.MoneyBackCreditCard;
import com.bank.creditCard.factory.PlatinumTimesCreditCard;
import com.bank.creditCard.factory.WorldMasterCreditCard;

import lombok.Getter;

import lombok.Setter;

@Component
@Setter
@Getter
public class CreditCardOffers {

	private String offerDetails = "Welcome e-Gift Voucher worth Rs. 5,000 on joining\r\n"
			+ "Get free movie tickets worth Rs. 6,000 every year\r\n"
			+ "Earn upto 50,000 Bonus Reward Points worth Rs. 12,500/year\r\n"
			+ "Complimentary membership to Club Vistara and Trident Privilege program";
	
	public String getOffersByCardId(String cardId) {

		String offerData = null;
		
		switch (cardId) {
		case "BUSINESS_GOLD":
			offerData = "Welcome e-gift Voucher worth Rs. 3,000 from Yatra for Business\r\n"
						+ "10 reward points on Dining, Utilities and Office supplies\r\n"
						+ "Complimentary international & domestic lounge access\r\n"
						+ "Complimentary access to MasterCard Global linker program" ;
		case "MONEYBACK":
			offerData = "Welcome e-Gift Voucher worth Rs. 3,000 on joining\r\n"
						+ "Spend linked Gift Vouchers worth Rs. 11,000\r\n"
						+ "10 Reward Points per Rs. 100 spent on Dining, Groceries, Departmental stores and Movies\r\n"
						+ "Complimentary International and Domestic Airport Lounge access";
		case "PLATINUM_TIMES":
			offerData = "Welcome e-Gift Voucher worth Rs. 5,000 on joining\r\n"
						+ "Get free movie tickets worth Rs. 6,000 every year\r\n"
						+ "Earn upto 50,000 Bonus Reward Points worth Rs. 12,500/year\r\n"
						+ "Complimentary membership to Club Vistara and Trident Privilege program";
		case "WORLD_MASTER":
			return "6000 Reward Points as a Welcome benefit worth INR 1500.\r\n"
					+ "Get 25X Reward Points on BPCL Fuel, Lubricants & Bharat Gas spends.\r\n"
					+ "Get 10X Reward Points on Departmental & Grocery, Movies & Dining Spends.";

		default:
			offerData = "Welcome e-Gift Voucher worth Rs. 5,000 on joining\r\n"
					+ "Get free movie tickets worth Rs. 6,000 every year\r\n"
					+ "Earn upto 50,000 Bonus Reward Points worth Rs. 12,500/year\r\n"
					+ "Complimentary membership to Club Vistara and Trident Privilege program";
		}
		this.offerDetails = offerData;
		return  offerData;
	}
}
