package com.bank.creditCard.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.entities.CardRewardPoints;
import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.exceptions.CardDeclinedException;
import com.bank.creditCard.exceptions.DataNotFound;
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
 * This is a resource class where the up-steam application request for Credit
 * Card transaction services like <br>
 * 1. Submit a Credit Card Transaction <br>
 * 2. Search for a Credit Card Transactions<br>
 * 3. View Reward points for a Credit Card <br>
 *
 */
@Tag(name = "Transactions Resource", description = "APIs which are used to submit/search/view a Credit Card Transactions and Reward Points")
@RestController
@RequestMapping(path = "/transaction")
public class CardTransactionResoruce {
 @Autowired
 CardTransactionService service;
 /**
  * Submit the transaction details for a credit card
  * @param cardTransactionDetails {@link RequestBody} serialize to
  *                               {@link TransactionDetails}
  * @return Transaction status details binded in {@link ResponseEntity}
  * @throws InterruptedException When Async Service Thread is interrupted while
  *                              it's waiting, sleeping
  * @throws ExecutionException   When Async Service is failed
  */
 @Operation(summary = "This method is used to submit a Credit Card transaction")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "201 CREATED", description = MessageConstants.TRANSACTION_COMPLETED),
         @ApiResponse(responseCode = "400 Bad Request", description = "Credit Card is declined")})
 @PostMapping(path = "/submit")
 public ResponseEntity<ServiceResponse> submitCreditCardTransaction(
        @Parameter(description = "Credit card transaction details with Card details", required = true,
                schema = @Schema(implementation = TransactionInputDetails.class))
         @RequestBody TransactionInputDetails cardTransactionDetails)
   throws InterruptedException, ExecutionException {
  CompletableFuture<TransactionInputDetails> transactionDetails = service
    .submitCreditCardTransaction(cardTransactionDetails);
  ResponseEntity<ServiceResponse> response = null;

  if (Objects.equals(transactionDetails.get().getTransactionStatus(),
    Constants.TRANSACTION_STATUS_FAILURE)) {
   response = Utility.getResponseEntity(
     new CardDeclinedException(transactionDetails.get().getTransactionMsg()),
     HttpStatus.BAD_REQUEST, transactionDetails.get().getTransactionMsg());
  } else {
   response = Utility.getResponseEntity(transactionDetails.get(),
     HttpStatus.CREATED, MessageConstants.TRANSACTION_COMPLETED);
  }

  return response;
 }

 /**
  * This method will used to search for a credit card transactions
  * @param searchQuery {@link RequestBody} serialize to
  *                    {@link TransactionSearchQuery}
  * @return List of Transaction details are bind in {@link ResponseEntity}
  */
 @Operation(summary = "This method is used to search a Credit card transactions")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "200 OK", description = MessageConstants.REQUEST_COMPLETED)})
 @PostMapping("/card/statement/")
 public ResponseEntity<ServiceResponse> getCardStatementsPerCard(
    @Parameter(description = "Search criteria of a Credit card", required = true,
     schema = @Schema(implementation = TransactionSearchQuery.class))
         @RequestBody TransactionSearchQuery searchQuery) {
  TransactionSearchResponse results = service.getStatementPerCard(searchQuery);
  return Utility.getResponseEntity(results, HttpStatus.OK,
    MessageConstants.REQUEST_COMPLETED);
 }

 /**
  * This method is used to get the available rewards on a credit card
  * @param cardId This is user credit cardId
  * @return {@link CardRewardPoints} is binded in {@link ResponseEntity}
  * @throws DataNotFound When the input credit cardId is invalid
  */
 @Operation(summary = "This method is used to get reward points on a Credit card")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "302 Found", description = MessageConstants.DATA_FOUND),
         @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND)
 })
 @GetMapping("/rewardpoints")
 public ResponseEntity<ServiceResponse> getRewardPointsPerCard(
   @Parameter(description = "CardId of a Credit Card", required = true, example = "10")
         @RequestParam(name = "cardId", required = true) Long cardId)
   throws DataNotFound {
  return service.getRewardPointsForCard(cardId);
 }
}
