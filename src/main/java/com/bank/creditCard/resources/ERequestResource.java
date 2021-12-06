package com.bank.creditCard.resources;

import java.util.Optional;

import javax.validation.Valid;

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

import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.exceptions.DataNotFound;
import com.bank.creditCard.exceptions.DeficientException;
import com.bank.creditCard.exceptions.DuplicateUserException;
import com.bank.creditCard.io.entities.CreditCardRequestDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.services.CreditCardRequestService;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This is a resource class which contains rest end points. Client application
 * can call this resources to get details of <br>
 * 1. Available Credit Cards in our service <br>
 * 2. Credit Card details <br>
 * 3. Raise a request for new credit card <br>
 * 4. Track the request status <br>
 */
@Tag(name = "eRequest Resource", description = "APIs to submit a request for Credit Card and can track its status")
@RestController
@RequestMapping(path = "/eRequest")
public class ERequestResource {
@Autowired
CreditCardRequestService service;

/**
 * This method will return available Credit Cards list in the system.
 *
 * @return {@link ResponseEntity} with available Credit Cards in the system.
 */
@Operation(summary = "This method will return available Credit Cards list in the system.")
@ApiResponse(responseCode = "302", description = MessageConstants.DATA_FOUND)
@GetMapping("/available/cards")
public ResponseEntity<ServiceResponse> getAvailableCards() {
  return Utility.getResponseEntity(service.getAvaliableCards(),
          HttpStatus.FOUND, MessageConstants.DATA_FOUND);
}

/**
 * This method will return a Credit Card details and its offers.
 * <p>
 * The credit card offers will vary base on location parameter
 * </p>
 *
 * @param cardId   This is a Credit Card Id
 * @param location City Name to get the offer a particular city
 * @return {@link ResponseEntity} with a Credit Card details and its offers.
 * @throws DataNotFound This custom exception will occur when an cardId value
 *                      is invalid
 */
@Operation(summary = "This method will return a Credit Cards details and it's offers based on location")
@ApiResponses(value = {
        @ApiResponse(responseCode = "302 Found", description = MessageConstants.DATA_FOUND),
        @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND)})
@GetMapping("/cards/details/{cardId}")
public ResponseEntity<ServiceResponse> getCardsDetails(
        @Parameter(description = "Credit Card id from available Credit Cards", required = true, example = "PLATINUM_TIMES") @PathVariable String cardId,
        @Parameter(description = "City name to get offer details at particular location", example = "Delhi", required = false) @RequestParam(name = "location", defaultValue = "Hyderabad") String location)
        throws DataNotFound {
  return service.getCardDetailsByLocation(cardId, location);
}

/**
 * This method is used to submit a new request to apply for a Credit Card
 *
 * @param creditCardRequestDetails {@link RequestBody} serialize to
 *                                 {@link CreditCardRequestDetails}
 * @return {@link ResponseEntity} with an object of {@link CardRequestDetails}
 * @throws DuplicateUserException When an input PAN CARD number is already
 *                                exist in system.
 * @throws DeficientException     When Customer Salary is not eligible for a
 *                                Credit Card.
 * @throws DataNotFound           When Requested Card Name is not matches with
 *                                the system.
 */
@Operation(summary = "This method is to submit a request for Credit Cards")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201 Created", description = MessageConstants.CREDIT_CARD_REQUEST_CREATED_NEW_CUSTOMER),
        @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND),
        @ApiResponse(responseCode = "226 IM Used", description = "A customer is exist with this PAN CARD number"),
        @ApiResponse(responseCode = "206 Partial Content", description = "Customer Annual Salary must be greater than Rs. 50000")})
@PostMapping("/apply/card")
public ResponseEntity<ServiceResponse> postCreditCardRequestForNewCustomer(
        @Parameter(description = "Customer details with credit card request details", required = true, schema = @Schema(implementation = CreditCardRequestDetails.class)) @Valid @RequestBody CreditCardRequestDetails creditCardRequestDetails)
        throws DuplicateUserException, DeficientException, DataNotFound {
  CardRequestDetails cardRequestDetails = service
          .saveCreditCardRequestDetails(creditCardRequestDetails);
  return Utility.getResponseEntity(cardRequestDetails, HttpStatus.CREATED,
          MessageConstants.CREDIT_CARD_REQUEST_CREATED_NEW_CUSTOMER);
}

/**
 * This method will get the Request Details Status
 *
 * @param requestId Request Id that is generated when request is placed.
 * @return {@link CardRequestDetails} binded in {@link ResponseEntity}
 * @throws DataNotFound When requestId value is not found in system.
 */
@Operation(summary = "This method is to track the status of Credit Card request")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200 OK", description = MessageConstants.DATA_FOUND),
        @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND)})
@GetMapping("/id/{requestId}")
public ResponseEntity<ServiceResponse> getRequestDetails(
        @Parameter(description = "RequestId id of a Credit Card request", required = true, example = "01d077e0-e42a-4628-badd-375a3ee5bdd1") @PathVariable String requestId)
        throws DataNotFound {
  Optional<CardRequestDetails> cardRequestDetails = service
          .getRequestDetails(requestId);

  if (cardRequestDetails.isPresent()) {
    return Utility.getResponseEntity(cardRequestDetails.get(), HttpStatus.OK,
            MessageConstants.DATA_FOUND);
  } else {
    throw new DataNotFound("Could not find the requested Id in our system.");
  }

}
}
