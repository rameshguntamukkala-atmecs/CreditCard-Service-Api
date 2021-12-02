package com.bank.creditCard.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.Exceptions.CardDeclinedException;
import com.bank.creditCard.Exceptions.DataNotFound;
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

@RestController
@RequestMapping (path = "/transaction")
public class CardTransactionResoruce {

	@Autowired
	CardTransactionService service;
	
	/**
	 * Submit the transaction details for a credit card
	 * @param cardTransactionDetails
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
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
	 * Search for statements based on query parameters
	 * @param searchQuery
	 * @return
	 */
	@PostMapping("/card/statement/")
	public ResponseEntity<ServiceResponse> getCardStatementsPerCard (@RequestBody TransactionSearchQuery searchQuery) {
		
		TransactionSearchResponse results = service.getStatementPerCard(searchQuery);
		
		return Utility.getResponseEntity(results, HttpStatus.OK, MessageConstants.REQUEST_COMPLETED);
	}
	
	/**
	 * Get the reward points available for the card
	 * @param cardId
	 * @return
	 * @throws DataNotFound
	 */
	@GetMapping ("/rewardpoints")
	public ResponseEntity<ServiceResponse> getRewardPointsPerCard(@RequestParam (name = "cardId", required = true) Long cardId ) throws DataNotFound{
		return service.getRewardPointsForCard(cardId);
	}
	
}
