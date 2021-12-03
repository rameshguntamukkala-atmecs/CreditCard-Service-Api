package com.bank.creditCard.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.InvalidRequestException;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.repositories.CreditCardDetailsRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;
import com.bank.creditCard.validators.RequestValidator;

@Service
public class CreditCardUserService {

	@Autowired
	CustomerDetailsRepository customerDetailsRepository;
	
	@Autowired
	RequestValidator validator;
	
	@Autowired
	CreditCardDetailsRepository creditCardDetailsRepository;
	
	public ResponseEntity<ServiceResponse> getUserDetails(Long userId) throws DataNotFound {
		
		Optional<CustomerDetails> customerDetails =  customerDetailsRepository.findById(userId);
		
		if(customerDetails.isPresent()) {
			return Utility.getResponseEntity(customerDetails.get(), HttpStatus.FOUND, MessageConstants.DATA_FOUND);
		} else {
			throw new DataNotFound("User not found for the userId: "+ userId);
		}
	}

	@Transactional
	public ResponseEntity<ServiceResponse> validateAndUpdateUserDetails(Long userId, CustomerDetails customerDetails) throws DataNotFound, InvalidRequestException {
		
		validator.userDetailsForUpdate(userId, customerDetails) ;
		customerDetailsRepository.save(customerDetails);
		
		return Utility.getResponseEntity(customerDetails, HttpStatus.CREATED, MessageConstants.USER_DETAILS_UPDATE_SUCCESS);
	}

	public ResponseEntity<ServiceResponse> getCardsAvailableForUser(Long userId) {
		
		List<CreditCardDetails> userCreditCardsList =  creditCardDetailsRepository.findCardsByUserId(userId);
		
		return Utility.getResponseEntity(userCreditCardsList, HttpStatus.OK, null);
	}

	public ResponseEntity<ServiceResponse> getCreditCardDetails(Long cardId) {

		Optional<CreditCardDetails> creditCardDetails = creditCardDetailsRepository.findById(cardId);
		
		if(creditCardDetails.isEmpty()) {
			return Utility.getResponseEntity(new DataNotFound(), HttpStatus.NOT_FOUND, MessageConstants.DATA_NOT_FOUND);
		}
		
		return Utility.getResponseEntity(creditCardDetails.get(), HttpStatus.FOUND, MessageConstants.DATA_FOUND);
	}

}
