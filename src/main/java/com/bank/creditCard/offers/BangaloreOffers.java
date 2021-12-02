package com.bank.creditCard.offers;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class BangaloreOffers extends CreditCardOffers{

	private String offerDetails = "Earn 7% back on all Ola rides\r\n"
			+ "Earn 1% back on all other Spends\r\n"
			+ "Zero Annual Fee for the first year";
	
	
	@Override
	public String getOffersByCardId(String cardId) {
		
		String offerData = null;
		
		switch (cardId) {
		case "BUSINESS_GOLD":
			offerData = "Complimentary Club Marriott membership for first year\r\n"
					+ "Complimentary nights & weekend buffet at participating ITC hotels\r\n"
					+ "Unlimited Complimentary golf games at leading courses across India and select courses across the world\r\n"
					+ "Reward Points for every ₹ 150 spent\r\n"
					+ "Global Personal Concierge -24 X 7" ;
		case "MONEYBACK":
			offerData = "Upto 5% Unlimited Cashback everytime\r\n"
					+ "Paytm First Membership - Upto 75k worth benefits\r\n"
					+ "8 Complimentary Airport Lounge Access\r\n"
					+ "Gift Vouchers worth Rs.500 every Calendar Quarter";
		case "PLATINUM_TIMES":
			offerData = "Upto 5% Unlimited Cashback everytime\r\n"
					+ "12 Complimentary Airport Lounge Access\r\n"
					+ "Paytm First Membership - Upto 75k worth benefits\r\n"
					+ "Personal Accident Death Cover up to Rs.30 Lacs\r\n"
					+ "Upto 50 days of credit free period";
		case "WORLD_MASTER":
			return "2.5% 6E Rewards on IndiGo\r\n"
					+ "gift2% 6E Rewards on Grocery, Dining & Entertainment\r\n"
					+ "gift1% 6E Rewards on all other spends*\r\n"
					+ "Up to 10% 6E Rewards on IndiGo featured partners\r\n"
					+ "Discounted Convenience Fee on IndiGo tickets-Rs.150 per pax (current – Rs.300)";

		default:
			offerData =  "Earn 7% back on all Ola rides\r\n"
					+ "Earn 1% back on all other Spends\r\n"
					+ "Zero Annual Fee for the first year";
		}
		
		this.offerDetails = offerData; 
		return  offerData;
	
	}


}
