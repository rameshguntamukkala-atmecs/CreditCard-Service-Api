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

@RestController
@RequestMapping(path = "/eRequest")
public class ERequestResource {

	@Autowired
	CreditCardRequestService service;
	
	/**
	 * Get Available Cards in the System
	 * @return
	 */
	@GetMapping("/available/cards")
	public ResponseEntity<ServiceResponse> getAvailableCards() {
		return Utility.getResponseEntity(service.getAvaliableCards(), HttpStatus.FOUND, MessageConstants.DATA_FOUND);
	}
	
	/**
	 * Get a Credit Card Details along with offers list 
	 * @param cardId
	 * @param location
	 * @return
	 * @throws DataNotFound
	 */
	@GetMapping("/cards/details/{cardId}")
	public ResponseEntity<ServiceResponse> getCardsDetails(@PathVariable String cardId,
			@RequestParam(name = "location", defaultValue = "Hyderabad") String location) throws DataNotFound {
		return service.getCardDetailsByLocation(cardId, location);
	}
	
	/**
	 * Submit a request for a new user and credit card
	 * @param creditCardRequestDetails
	 * @return
	 * @throws DuplicateUserException
	 * @throws DeficientException
	 * @throws DataNotFound
	 */
	@PostMapping("/apply/card")
	public ResponseEntity<ServiceResponse> postCreditCardRequestForNewCustomer (@Valid @RequestBody CreditCardRequestDetails creditCardRequestDetails) throws DuplicateUserException, DeficientException, DataNotFound {
	
		CardRequestDetails cardRequestDetails =  service.saveCreditCardRequestDetails(creditCardRequestDetails);
		
		return Utility.getResponseEntity(cardRequestDetails, HttpStatus.CREATED, MessageConstants.CREDIT_CARD_REQUEST_CREATED_NEW_CUSTOMER);
	}
	
	/**
	 * Get the status and details of a request
	 * @param requestId
	 * @return
	 * @throws DataNotFound
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
