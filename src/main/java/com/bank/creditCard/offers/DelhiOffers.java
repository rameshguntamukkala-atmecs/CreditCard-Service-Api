package com.bank.creditCard.offers;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class DelhiOffers extends CreditCardOffers {
 private String offerDetails = "Up to 50 days interest free credit period\r\n"
   + "1% CashBack on all Best Price spends\r\n" + "Free Best Price vouchers\r\n"
   + "1% Fuel surcharge waiver";
 @Override
 public String getOffersByCardId(String cardId) {
  String offerData = null;

  switch (cardId) {
   case "BUSINESS_GOLD" :
    offerData = "Welcome benefit of up to 25,000 InterMiles and earn 8 InterMiles per every Rs.150 spent\r\n"
      + "InterMiles Gold tier Membership for first year\r\n"
      + "2X InterMiles for flights booked on www.intermiles.com & Hotel booked via Interbook.intermiles.com\r\n"
      + "Rs 5000 Worth Flight & Hotel discount vouchers*";
   case "MONEYBACK" :
    offerData = "Up to 50 days interest free credit period\r\n"
      + "4 Reward Points for every Rs.150 spent\r\n"
      + "Complimentary 6 International + 12 domestic airport lounge visits annually\r\n"
      + "5% CashBack on Telecom, Electricity, Government/Tax, Railway, Hotels & Dining and Taxi spends\r\n"
      + "Travel insurance cover up to Rs. 1 Cr";
   case "PLATINUM_TIMES" :
    offerData = "Up to 50 days interest free credit period\r\n"
      + "4 Reward Points for every Rs.150 spent on all online purchases\r\n"
      + "2 Reward Points for every Rs 150 spent on other expenses (Except fuel)\r\n"
      + "1% Fuel surcharge waiver\r\n"
      + "5% CashBack on Telecom, Electricity, Government/Tax, Railway, Hotels & Dining and Taxi spends";
   case "WORLD_MASTER" :
    return "Welcome benefit of up to 8,000 InterMiles and earn 6 InterMiles per every Rs.150 spent\r\n"
      + "InterMiles Silver tier Membership for first year\r\n"
      + "2X InterMiles for flights booked on www.intermiles.com & Hotel booked via Interbook.intermiles.com\r\n"
      + "Complimentary Domestic & International Lounge Access*";
   default :
    offerData = "Up to 50 days interest free credit period\r\n"
      + "1% CashBack on all Best Price spends\r\n"
      + "Free Best Price vouchers\r\n" + "1% Fuel surcharge waiver";
  }

  this.offerDetails = offerData;
  return offerData;
 }
}
