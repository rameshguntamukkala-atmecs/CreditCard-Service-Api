package com.bank.creditCard.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.creditCard.configuration.RewardPointsProperties;

@Component
public class RewardPointsFactory {

	@Autowired
	RewardPointsProperties rewardPointsProperties;
	
	public RewardPointsCalculator getCreditCardTypeObject(String creditCardName) {
		System.out.println("Porperties: "+ rewardPointsProperties);
		switch (creditCardName) {
		case "BUSINESS_GOLD":
				return new BusinessGoldCreditCard(rewardPointsProperties.getBusinessGold());
		case "MONEYBACK":
				return new MoneyBackCreditCard(rewardPointsProperties.getMoneyback());
		case "PLATINUM_TIMES":
				return new PlatinumTimesCreditCard(rewardPointsProperties.getPlatinumTimes());
		case "WORLD_MASTER":
			return new WorldMasterCreditCard(rewardPointsProperties.getWorldMaster());

		default:
			return null;
		}
	}
}
