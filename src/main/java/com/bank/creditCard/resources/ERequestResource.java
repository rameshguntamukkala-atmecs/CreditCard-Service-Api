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

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.DeficientException;
import com.bank.creditCard.Exceptions.DuplicateUserException;
import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.io.entities.CreditCardRequestDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.services.CreditCardRequestService;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;

/**
 * 
 * This is a resource class which contains rest end points. 
 * Client application can call this resources to get details of <br>
 * 1. Available Credit Cards in our service <br>
 * 2. Credit Card details <br>
 * 3. Raise a request for new credit card <br>
 * 4. Track the request status <br>
 *
 */
@RestController
@RequestMapping(path = "/eRequest")
public class ERequestResource {

	@Autowired
	CreditCardRequestService service;
	
	/**
	 * This method will return available Credit Cards list in the system.
	 * @return {@link ResponseEntity} with available Credit Cards in the system.
	 *  
	 */
	@GetMapping("/available/cards")
	public ResponseEntity<ServiceResponse> getAvailableCards() {
		return Utility.getResponseEntity(service.getAvaliableCards(), HttpStatus.FOUND, MessageConstants.DATA_FOUND);
	}
	
	/**
	 * This method will return a Credit Card details and its offers. 
	 * <p>The credit card offers will vary base on location parameter</p> 
	 * @param cardId This is a Credit Card Id
	 * @param location City Name to get the offer a particular city
	 * @return {@link ResponseEntity} with a Credit Card details and its offers. 
	 * @throws DataNotFound This custom exception will occur when an cardId value is invalid 
	 */
	@GetMapping("/cards/details/{cardId}")
	public ResponseEntity<ServiceResponse> getCardsDetails(@PathVariable String cardId,
			@RequestParam(name = "location", defaultValue = "Hyderabad") String location) throws DataNotFound {
		return service.getCardDetailsByLocation(cardId, location);
	}
	
	/**
	 * This method is used to submit a new request to apply for a Credit Card 
	 * @param creditCardRequestDetails {@link RequestBody} serialize to {@link CreditCardRequestDetails}
	 * @return {@link ResponseEntity} with an object of {@link CardRequestDetails}  
	 * @throws DuplicateUserException When an input PAN CARD number is already exist in system.
	 * @throws DeficientException When Customer Salary is not eligible for a Credit Card.
	 * @throws DataNotFound When Requested Card Name is not matches with the system.
	 */
	@PostMapping("/apply/card")
	public ResponseEntity<ServiceResponse> postCreditCardRequestForNewCustomer (@Valid @RequestBody CreditCardRequestDetails creditCardRequestDetails) throws DuplicateUserException, DeficientException, DataNotFound {
	
		CardRequestDetails cardRequestDetails =  service.saveCreditCardRequestDetails(creditCardRequestDetails);
		
		return Utility.getResponseEntity(cardRequestDetails, HttpStatus.CREATED, MessageConstants.CREDIT_CARD_REQUEST_CREATED_NEW_CUSTOMER);
	}
	
	/**
	 * This method will get the Request Details Status
	 * @param requestId Request Id that is generated when request is placed. 
	 * @return {@link CardRequestDetails} binded in {@link ResponseEntity}
	 * @throws DataNotFound When requestId value is not found in system.
	 */
	@GetMapping("/id/{requestId}")
	public ResponseEntity<ServiceResponse> getRequestDetails( @PathVariable String requestId) throws DataNotFound {
		
		Optional<CardRequestDetails> cardRequestDetails = service.getRequestDetails(requestId);
		
		if(cardRequestDetails.isPresent()) {
			return Utility.getResponseEntity(cardRequestDetails.get(), HttpStatus.OK, MessageConstants.DATA_FOUND);
		} else {
			throw new DataNotFound("Could not find the requested Id in our system.");
		}
	}
}
