package com.bank.creditCard.schedular;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.factory.RewardPointsFactory;
import com.bank.creditCard.factory.RewardPointsCalculator;
import com.bank.creditCard.io.entities.CardRewardPoints;
import com.bank.creditCard.repositories.RewardPointsRepository;
import com.bank.creditCard.repositories.TransactionRepository;
import com.bank.creditCard.utilities.Constants;

@Component
public class RewardPointsSchedularService {

	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	RewardPointsRepository rewardPointsRepository;
	
	@Autowired
	RewardPointsFactory factory;

	private static Logger logger = LoggerFactory.getLogger(RewardPointsSchedularService.class);

	
//	@Async("asyncExecutor")
	@Transactional
	public void processTransactions(List<TransactionDetails> transactions, CreditCardDetails userCardDetails,
			CreditCardName cardNameDetails) {

		
		BigDecimal totalGroceriesAmount  = transactions.stream().filter( trans -> Constants.TRANSACTION_CATEGORY_GROCERIES.equals(trans.getTransactionCategory()))
				.map(TransactionDetails::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		
		BigDecimal totalFuelAmount  = transactions.stream().filter( trans -> Constants.TRANSACTION_CATEGORY_FUEL.equals(trans.getTransactionCategory()))
				.map(TransactionDetails::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal totalShoppingAmount  = transactions.stream().filter( trans -> Constants.TRANSACTION_CATEGORY_SHOPPING.equals(trans.getTransactionCategory()))
				.map(TransactionDetails::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal totalTravellingAmount  = transactions.stream().filter( trans -> Constants.TRANSACTION_CATEGORY_TRAVELLING.equals(trans.getTransactionCategory()))
				.map(TransactionDetails::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal totalOtherAmount  = transactions.stream().filter( trans -> (Constants.TRANSACTION_CATEGORY_OTHER.equals(trans.getTransactionCategory()) || 
				Constants.TRANSACTION_CATEGORY_RESTARENTS.equals(trans.getTransactionCategory()) )).map(TransactionDetails::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		if(cardNameDetails != null ) {
			RewardPointsCalculator calculator =  factory.getCreditCardTypeObject(cardNameDetails.getCreditCardId());
			
			
			BigInteger groceriesRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_GROCERIES, totalGroceriesAmount);
			BigInteger fuelRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_FUEL, totalFuelAmount);
			BigInteger travelRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_TRAVELLING, totalTravellingAmount);
			BigInteger shoppingRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_SHOPPING, totalShoppingAmount);
			BigInteger otherRewardPoints = calculator.getRewardPointsForAmount(Constants.TRANSACTION_CATEGORY_OTHER, totalOtherAmount);
			
			BigInteger totalRewardPoints = Arrays.asList(groceriesRewardPoints, fuelRewardPoints, travelRewardPoints, shoppingRewardPoints, otherRewardPoints)
											.stream()
											.reduce(BigInteger.ZERO, BigInteger::add);
			
			CardRewardPoints rewardPoints =  rewardPointsRepository.findByCardId(userCardDetails.getUserCardId()).orElse(null);
			
			if(rewardPoints != null) {
				rewardPoints.setRewardPoints(rewardPoints.getRewardPoints().add(totalRewardPoints));
				rewardPoints.setModifiedTime(new Timestamp(System.currentTimeMillis()));
			} else {
				rewardPoints = getNewRewardPointsObject(totalRewardPoints, userCardDetails.getUserCardId());
			}
			
			rewardPointsRepository.save(rewardPoints);
			
			List<Long> transactionIds = transactions.stream().map(TransactionDetails::getTransactionId).collect(Collectors.toList());  
			
			transactionRepository.updateRewardPointStaus(Constants.REWARD_POINT_STATUS_ADDED, transactionIds );
			
		} else {
			logger.error("Credit card details are not found", new Exception());
		}
	}

	private CardRewardPoints getNewRewardPointsObject(BigInteger totalRewardPoints, Long userCardId) {
		CardRewardPoints rewardPoints = new CardRewardPoints();
		
		rewardPoints.setCardId(userCardId);
		rewardPoints.setRewardPoints(totalRewardPoints);
		rewardPoints.setCreatedTime(new Timestamp(System.currentTimeMillis()));
		rewardPoints.setModifiedTime(new Timestamp(System.currentTimeMillis()));
		
		return rewardPoints;
	}
	
}
