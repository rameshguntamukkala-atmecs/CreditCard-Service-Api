package com.bank.creditCard.factory;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface RewardPointsCalculator {
 public BigInteger getRewardPointsForAmount(Short transactionCategory,
   BigDecimal totalAmount);
}
