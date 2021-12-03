package com.bank.creditCard.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.entities.CardRewardPoints;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.io.entities.TransactionInputDetails;
import com.bank.creditCard.io.entities.TransactionSearchQuery;
import com.bank.creditCard.io.entities.TransactionSearchResponse;
import com.bank.creditCard.repositories.CreditCardDetailsRepository;
import com.bank.creditCard.repositories.RewardPointsRepository;
import com.bank.creditCard.repositories.TransactionRepository;
import com.bank.creditCard.repositories.TransactionSearchRepository;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.EntityHelper;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;
import com.bank.creditCard.validators.RequestValidator;

@Service
public class CardTransactionService {

	@Autowired
	RequestValidator validator;
	
	@Autowired
	EntityHelper helper;
	
	@Autowired
	TransactionRepository repository;
	
	@Autowired
	RewardPointsRepository rewardPointsRepository;
	
	@Autowired
	CreditCardDetailsRepository creditCardDetailsRepository;
	
	@Autowired
	TransactionSearchRepository transactionSearchRepository;
	
	/**
	 * This method is used to save the Credit Card transaction in {@link TransactionDetails}
	 * <p>This method will validate {@link TransactionInputDetails} and save the {@link TransactionDetails} with transaction status. 
	 * If the transaction status is success the, service will calculate the Outstanding amount and updated  the Customer {@link CreditCardDetails}</p>
	 * @param inputTransactionDetails Credit Card transaction details
	 * @return Transaction details binded in {@link CompletableFuture} as it is Asynchronous method
	 */
	@Transactional
	public  CompletableFuture<TransactionInputDetails> submitCreditCardTransaction(TransactionInputDetails inputTransactionDetails)  {
	
		TransactionDetails transactionDetails = helper.generateTransactionDetails(inputTransactionDetails);
		
		validator.validateCreditCardTransaction(inputTransactionDetails);
		inputTransactionDetails.setTransactionTime(transactionDetails.getTransactionTime());
		
		helper.generateTransactionDetailsWithStatus(inputTransactionDetails, transactionDetails);
		
		repository.save(transactionDetails);

		if(Constants.TRANSACTION_STATUS_SUCCESS.equals(transactionDetails.getTransactionStatus()) ) {
			BigDecimal outStandingAmount = null;
			
			if(Constants.TRANSACTION_TYPE_DEBIT.equals(transactionDetails.getTransactionType())) {
				outStandingAmount = inputTransactionDetails.getCreditCardDetails().getOutStandingAmount().add(inputTransactionDetails.getAmount());
			} else if (Constants.TRANSACTION_TYPE_CREDIT.equals(transactionDetails.getTransactionType())) {
				outStandingAmount = inputTransactionDetails.getCreditCardDetails().getOutStandingAmount().subtract(inputTransactionDetails.getAmount());
			}
			
			inputTransactionDetails.getCreditCardDetails().setOutStandingAmount(outStandingAmount);
			inputTransactionDetails.getCreditCardDetails().setOutStandingAmountModifiedTime(new Timestamp(System.currentTimeMillis()));
			creditCardDetailsRepository.save(inputTransactionDetails.getCreditCardDetails());
		}
		
		return CompletableFuture.completedFuture(inputTransactionDetails) ;
	}

	/**
	 * This method will get Reward Points generated on Credit Card
	 * @param cardId This is carId of a {@link CreditCardDetails}
	 * @return {@link CardRewardPoints} of a Credit card is binded in {@link ResponseEntity}
	 * @throws DataNotFound When no Reward points found for a Credit Card
	 */
	public ResponseEntity<ServiceResponse> getRewardPointsForCard(Long cardId) throws DataNotFound {
		Optional<CardRewardPoints>  cardRewardPoints = rewardPointsRepository.findByCardId(cardId);
		
		if(cardRewardPoints.isEmpty()) {
			throw new DataNotFound();
		}
		
		return Utility.getResponseEntity(cardRewardPoints.get(), HttpStatus.FOUND, MessageConstants.DATA_FOUND);
	}

	/**
	 * This method is used to search for Credit Card transaction based on search criteria
	 * @param searchQuery Search criteria on Credit Card to find the {@link TransactionDetails}
	 * @return List of {@link TransactionDetails} binded in {@link TransactionSearchResponse}
	 */
	public TransactionSearchResponse getStatementPerCard(TransactionSearchQuery searchQuery) {

		List<TransactionDetails> transactionDetails = transactionSearchRepository.getTransactionDetails(searchQuery);

		CreditCardDetails cardDetails = creditCardDetailsRepository.findCardDetailsByCardNumber(searchQuery.getCardNumber());
		
		TransactionSearchResponse resultObject = helper.generateTransactionSearchResponse(transactionDetails, cardDetails); 
		
		return resultObject;
		
	}
	
	

}
