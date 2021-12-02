package com.bank.creditCard.resources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.InvalidRequestException;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.services.CreditCardUserService;

@RestController
@RequestMapping(path = "/user")
public class CreditCardUserResoruce {

	@Autowired
	CreditCardUserService service;
	
	/**
	 * Get User details
	 * @param userId
	 * @return
	 */
	@GetMapping ("/details/{userId}")
	public ResponseEntity<ServiceResponse> getUserDetails(@PathVariable Long userId){
		return service.getUserDetails(userId);
	}
	
	/**
	 * Update user details like phone number, emailId
	 * @param userId
	 * @param customerDetails
	 * @return
	 * @throws DataNotFound
	 * @throws InvalidRequestException
	 */
	@PutMapping ("/update/details/{userId}")
	public ResponseEntity<ServiceResponse> updateUserDetails(@PathVariable Long userId,
			@RequestBody CustomerDetails customerDetails) throws DataNotFound, InvalidRequestException {
		return service.validateAndUpdateUserDetails(userId, customerDetails);
	}
	
	/**
	 * Get all credit cards available for a user
	 * @param userId
	 * @return
	 */
	@GetMapping ("/cards/{userId}") 
	public ResponseEntity<ServiceResponse> getCardsAvailableForUser(@PathVariable Long userId) {
		return service.getCardsAvailableForUser(userId);
	}
	
	/**
	 * Get a user credit card details
	 * @param cardId
	 * @return
	 */
	@GetMapping ("/card/details/{cardId}") 
	public ResponseEntity<ServiceResponse> getCreditCardDetails(@PathVariable Long cardId) {
		return service.getCreditCardDetails(cardId);
	}
	
}
