package com.bank.creditCard.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.Exceptions.CardDeclinedException;
import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.entities.CardRewardPoints;
import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.io.entities.TransactionInputDetails;
import com.bank.creditCard.io.entities.TransactionSearchQuery;
import com.bank.creditCard.io.entities.TransactionSearchResponse;
import com.bank.creditCard.services.CardTransactionService;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
/**
 * 
 * This is a resource class where the up-steam application request for Credit Card transaction services like <br>
 * 1. Submit a Credit Card Transaction <br>
 * 2. Search for a Credit Card Transactions<br>
 * 3. View Reward points for a Credit Card <br>
 *
 */
@RestController
@RequestMapping (path = "/transaction")
public class CardTransactionResoruce {

	@Autowired
	CardTransactionService service;
	
	/**
	 * Submit the transaction details for a credit card
	 * @param cardTransactionDetails {@link RequestBody} serialize to {@link TransactionDetails} 
	 * @return Transaction status details binded in {@link ResponseEntity} 
	 * @throws InterruptedException When Async Service Thread is interrupted while it's waiting, sleeping
	 * @throws ExecutionException When Async Service is failed
	 */
	@PostMapping (path = "/submit")
	public ResponseEntity<ServiceResponse> submitCreditCardTransaction (@RequestBody TransactionInputDetails cardTransactionDetails) throws InterruptedException, ExecutionException  {
		
		CompletableFuture<TransactionInputDetails> transactionDetails =  service.submitCreditCardTransaction(cardTransactionDetails);
		
		ResponseEntity<ServiceResponse> response = null;
		if(Objects.equals(transactionDetails.get().getTransactionStatus(), Constants.TRANSACTION_STATUS_FAILURE) ) {
			response = Utility.getResponseEntity(new CardDeclinedException(transactionDetails.get().getTransactionMsg()), HttpStatus.BAD_REQUEST, transactionDetails.get().getTransactionMsg());
		} else {
			response = Utility.getResponseEntity(transactionDetails.get(), HttpStatus.CREATED, MessageConstants.TRANSACTION_COMPLETED);
		}
		return response;
	}

	/**
	 * This method will used to search for a credit card transactions
	 * @param searchQuery {@link RequestBody} serialize to {@link TransactionSearchQuery} 
	 * @return List of Transaction details are bind in {@link ResponseEntity}
	 */
	@PostMapping("/card/statement/")
	public ResponseEntity<ServiceResponse> getCardStatementsPerCard (@RequestBody TransactionSearchQuery searchQuery) {
		
		TransactionSearchResponse results = service.getStatementPerCard(searchQuery);
		
		return Utility.getResponseEntity(results, HttpStatus.OK, MessageConstants.REQUEST_COMPLETED);
	}
	
	/**
	 * This method is used to get the available rewards on a credit card 
	 * @param cardId This is user credit cardId
	 * @return {@link CardRewardPoints} is binded in {@link ResponseEntity}
	 * @throws DataNotFound When the input credit cardId is invalid
	 */
	@GetMapping ("/rewardpoints")
	public ResponseEntity<ServiceResponse> getRewardPointsPerCard(@RequestParam (name = "cardId", required = true) Long cardId ) throws DataNotFound{
		return service.getRewardPointsForCard(cardId);
	}
	
}
