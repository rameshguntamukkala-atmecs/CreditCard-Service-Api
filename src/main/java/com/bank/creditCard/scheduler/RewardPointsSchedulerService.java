package com.bank.creditCard.scheduler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.creditCard.entities.CardRewardPoints;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.factory.RewardPointsFactory;
import com.bank.creditCard.factory.RewardPointsCalculator;
import com.bank.creditCard.repositories.RewardPointsRepository;
import com.bank.creditCard.repositories.TransactionRepository;
import com.bank.creditCard.utilities.Constants;

/**
 * This is a service class used to calculate the reward points on different card
 * transactions
 */
@Component
public class RewardPointsSchedulerService {
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	RewardPointsRepository rewardPointsRepository;
	@Autowired
	RewardPointsFactory factory;
	private static Logger logger = LoggerFactory.getLogger(RewardPointsSchedulerService.class);

	/**
	 * This method will calculate reward points based on list of transactions
	 *
	 * @param transactions    List of {@link TransactionDetails} on a customer
	 *                        credit card
	 * @param userCardDetails {@link CreditCardDetails} of a Customer
	 * @param cardNameDetails {@link CreditCardName} of a Customer
	 */
	// @Async("asyncExecutor")
	@Transactional
	public void processTransactions(List<TransactionDetails> transactions, CreditCardDetails userCardDetails, CreditCardName cardNameDetails) {
		BigDecimal totalGroceriesAmount = transactions.stream().filter(trans -> Constants.TRANSACTION_CATEGORY_GROCERIES.equals(trans.getTransactionCategory())).map(TransactionDetails::getTransactionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalFuelAmount = transactions.stream().filter(trans -> Constants.TRANSACTION_CATEGORY_FUEL.equals(trans.getTransactionCategory())).map(TransactionDetails::getTransactionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalShoppingAmount = transactions.stream().filter(trans -> Constants.TRANSACTION_CATEGORY_SHOPPING.equals(trans.getTransactionCategory())).map(TransactionDetails::getTransactionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalTravellingAmount = transactions.stream().filter(trans -> Constants.TRANSACTION_CATEGORY_TRAVELLING.equals(trans.getTransactionCategory())).map(TransactionDetails::getTransactionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalOtherAmount = transactions.stream().filter(trans -> (Constants.TRANSACTION_CATEGORY_OTHER.equals(trans.getTransactionCategory()) || Constants.TRANSACTION_CATEGORY_RESTARENTS.equals(trans.getTransactionCategory()))).map(TransactionDetails::getTransactionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		if (cardNameDetails != null) {
			RewardPointsCalculator calculator = factory.getCreditCardTypeObject(cardNameDetails.getCreditCardId());
			BigInteger groceriesRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_GROCERIES, totalGroceriesAmount);
			BigInteger fuelRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_FUEL, totalFuelAmount);
			BigInteger travelRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_TRAVELLING, totalTravellingAmount);
			BigInteger shoppingRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_SHOPPING, totalShoppingAmount);
			BigInteger otherRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_OTHER, totalOtherAmount);
			BigInteger totalRewardPoints = Arrays.asList(groceriesRewardPoints, fuelRewardPoints, travelRewardPoints, shoppingRewardPoints, otherRewardPoints).stream().reduce(BigInteger.ZERO, BigInteger::add);
			CardRewardPoints rewardPoints = rewardPointsRepository.findByCardId(userCardDetails.getUserCardId()).orElse(null);

			if (rewardPoints != null) {
				rewardPoints.setRewardPoints(rewardPoints.getRewardPoints().add(totalRewardPoints));
			} else {
				rewardPoints = getNewRewardPointsObject(totalRewardPoints, userCardDetails.getUserCardId());
			}

			rewardPointsRepository.save(rewardPoints);
			List<Long> transactionIds = transactions.stream().map(TransactionDetails::getTransactionId).collect(Collectors.toList());
			transactionRepository.updateRewardPointsStatusByTransactionIdIn(Constants.REWARD_POINT_STATUS_ADDED, transactionIds);
		} else {
			logger.error("Credit card details are not found", new Exception());
		}

	}

	/**
	 * This method will generate the entity class for {@link CardRewardPoints}
	 *
	 * @param totalRewardPoints Total reward points on credit card
	 * @param userCardId        cardId of a credit card
	 * @return New {@link CardRewardPoints} entity
	 */
	private CardRewardPoints getNewRewardPointsObject(BigInteger totalRewardPoints, Long userCardId) {
		CardRewardPoints rewardPoints = new CardRewardPoints();
		rewardPoints.setCardId(userCardId);
		rewardPoints.setRewardPoints(totalRewardPoints);
		return rewardPoints;
	}
}
