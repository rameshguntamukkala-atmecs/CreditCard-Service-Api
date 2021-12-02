package com.bank.creditCard.offers;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class HyderabadOffers extends CreditCardOffers{

	private String offerDetails = "Welcome benefit of 500 Reward Points worth Rs. 500 on payment of joining fee.\r\n"
			+ "Complimentary OneApollo Gold Tier Membership.\r\n"
			+ "Get Upto 10% Instant Discount at Select Apollo Services.\r\n"
			+ "3X Reward Points on every Rs. 100 spent on all Apollo services. 1 RP = 1 Rs.";
	
	
	@Override
	public String getOffersByCardId(String cardId) {
		


		String offerData = null;
		
		switch (cardId) {
		case "BUSINESS_GOLD":
			offerData = "Welcome e-Gift Voucher worth Rs. 5,000 on joining\r\n"
					+ "Get free movie tickets worth Rs. 6,000 every year\r\n"
					+ "Earn upto 50,000 Bonus Reward Points worth Rs. 12,500/year" ;
		case "MONEYBACK":
			offerData = "Welcome e-Gift Voucher worth Rs. 3,000 on joining\r\n"
					+ "Spend linked Gift Vouchers worth Rs. 11,000\r\n"
					+ "10 Reward Points per Rs. 100 spent on Dining, Groceries, Departmental stores and Movies\r\n"
					+ "Complimentary International and Domestic Airport Lounge access";
		case "PLATINUM_TIMES":
			offerData = "Welcome e-Gift Voucher worth Rs. 3,000 on joining\r\n"
					+ "Spend linked Gift Vouchers worth Rs. 11,000\r\n"
					+ "10 Reward Points per Rs. 100 spent on Dining, Groceries, Departmental stores and Movies\r\n"
					+ "Complimentary International and Domestic Airport Lounge access";
		case "WORLD_MASTER":
			return "10%* back as Reward Points for railway ticket purchases\r\n"
					+ "Welcome benefit worth INR 2000*\r\n"
					+ "Milestone benefits worth INR 7500 on annual travel spends\r\n"
					+ "Zero payment gateway charges on railway and Airline booking on irctc.co.in*";

		default:
			offerData = "Welcome benefit of 500 Reward Points worth Rs. 500 on payment of joining fee.\r\n"
					+ "Complimentary OneApollo Gold Tier Membership.\r\n"
					+ "Get Upto 10% Instant Discount at Select Apollo Services.\r\n"
					+ "3X Reward Points on every Rs. 100 spent on all Apollo services. 1 RP = 1 Rs.";
		}
		
		this.offerDetails = offerData; 
		return  offerData;
	
	}
	
}
