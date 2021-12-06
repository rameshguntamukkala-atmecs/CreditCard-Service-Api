package com.bank.creditCard.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.creditCard.configuration.RewardPointsProperties;

/**
 * 
 * This is factory class component which provide a CreditCard instance to
 * calculate reward points
 *
 */
@Component
public class RewardPointsFactory {
 @Autowired
 RewardPointsProperties rewardPointsProperties;
 /**
  * A factory method which provide a card instance based on card name
  * @param creditCardName Credit Card Name
  * @return A card instance to calculate reward points
  */
 public RewardPointsCalculator getCreditCardTypeObject(String creditCardName) {
  System.out.println("Porperties: " + rewardPointsProperties);

  switch (creditCardName) {
   case "BUSINESS_GOLD" :
    return new BusinessGoldCreditCard(rewardPointsProperties.getBusinessGold());
   case "MONEYBACK" :
    return new MoneyBackCreditCard(rewardPointsProperties.getMoneyback());
   case "PLATINUM_TIMES" :
    return new PlatinumTimesCreditCard(
      rewardPointsProperties.getPlatinumTimes());
   case "WORLD_MASTER" :
    return new WorldMasterCreditCard(rewardPointsProperties.getWorldMaster());
   default :
    return null;
  }

 }
}
