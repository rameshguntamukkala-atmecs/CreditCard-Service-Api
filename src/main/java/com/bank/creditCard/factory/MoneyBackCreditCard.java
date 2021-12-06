package com.bank.creditCard.factory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.bank.creditCard.utilities.Constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * This method will calculate the RewardPoints for the Moneyback credit card
 * based on Transaction category
 *
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class MoneyBackCreditCard implements RewardPointsCalculator {
 private Double groceriesPercentage;
 private Double fuelPercentage;
 private Double travelPercentage;
 private Double shoppingPercentage;
 private Double otherPercentage;
 public MoneyBackCreditCard(Map<String, Double> properties) {
  this.groceriesPercentage = properties.get("groceries");
  this.fuelPercentage = properties.get("fuel");
  this.travelPercentage = properties.get("travel");
  this.shoppingPercentage = properties.get("shopping");
  this.otherPercentage = properties.get("other");
 }

 @Override
 public BigInteger getRewardPointsForAmount(Short transactionCategory,
   BigDecimal totalAmount) {
  BigInteger rewardPoints = BigInteger.ZERO;

  switch (transactionCategory) {
   case 1 :
    rewardPoints = calculateRewardPointsForAmount(getGroceriesPercentage(),
      totalAmount);
    break;
   case 2 :
    rewardPoints = calculateRewardPointsForAmount(getShoppingPercentage(),
      totalAmount);
    break;
   case 3 :
    rewardPoints = calculateRewardPointsForAmount(getTravelPercentage(),
      totalAmount);
    break;
   case 5 :
    rewardPoints = calculateRewardPointsForAmount(getFuelPercentage(),
      totalAmount);
    break;
   default :
    rewardPoints = calculateRewardPointsForAmount(getOtherPercentage(),
      totalAmount);
    break;
  }

  return rewardPoints;
 }

 private BigInteger calculateRewardPointsForAmount(Double percentage,
   BigDecimal totalAmount) {
  BigInteger rewardPoints = BigInteger.ZERO;

  if (percentage > 0 && totalAmount.compareTo(BigDecimal.ZERO) > 0) {
   rewardPoints = totalAmount.multiply(BigDecimal.valueOf(percentage))
     .divide(BigDecimal.valueOf(100.00d)).toBigInteger();
  }

  return rewardPoints;
 }
}
