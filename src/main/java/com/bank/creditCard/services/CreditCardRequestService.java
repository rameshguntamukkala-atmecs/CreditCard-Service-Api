package com.bank.creditCard.services;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Delayed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.DeficientException;
import com.bank.creditCard.Exceptions.DuplicateUserException;
import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.io.entities.CreditCardRequestDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.offers.BangaloreOffers;
import com.bank.creditCard.offers.CreditCardOffers;
import com.bank.creditCard.offers.DelhiOffers;
import com.bank.creditCard.offers.HyderabadOffers;
import com.bank.creditCard.repositories.CreditCardNamesRepository;
import com.bank.creditCard.repositories.CreditCardRequestRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.utilities.EntityHelper;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;
import com.bank.creditCard.validators.RequestValidator;

@Service
public class CreditCardRequestService {

	@Autowired
	CreditCardRequestRepository creditCardRequestRepository;
	
	@Autowired
	CustomerDetailsRepository customerDetailsRepository;
	
	@Autowired
	RequestValidator requestValidator;
	
	@Autowired
	CreditCardNamesRepository creditCardNamesRepository;
	
	@Autowired
	EntityHelper entityHelper;
	
	public Optional<CardRequestDetails> getRequestDetails(String requestId) {
		return creditCardRequestRepository.findById(requestId);
	}

	public List<CreditCardName> getAvaliableCards() {
		return creditCardNamesRepository.findAll();
	}

	@Transactional
	public CardRequestDetails saveCreditCardRequestDetails(CreditCardRequestDetails creditCardRequestDetails) throws DuplicateUserException, DeficientException, DataNotFound {
		
		requestValidator.validate(creditCardRequestDetails);
		
		CustomerDetails customerDetails = entityHelper.generatedCustomerDetails(creditCardRequestDetails);
		
		customerDetailsRepository.save(customerDetails);
		
		CardRequestDetails cardRequestDetails = entityHelper.generateCardRequestDetails(creditCardRequestDetails, customerDetails);
		
		creditCardRequestRepository.save(cardRequestDetails);
		
		return cardRequestDetails;
	}

	public List<CardRequestDetails> getRequestDetailsByStatus(Short status) {
		return creditCardRequestRepository.findAllByStatus(status);
	}

	@Transactional
	public void updateStatusToRequest(Short status, String requestId, String message) {
		creditCardRequestRepository.updateStatusToTheRequest(status, requestId, message);
	}

	public ResponseEntity<ServiceResponse> getCardDetailsByLocation(String cardId, String location) throws DataNotFound {
		
		Optional<CreditCardName> cardOptional = creditCardNamesRepository.findAll()
				.stream()
				.filter(card -> cardId.equals(card.getCreditCardId()))
				.findFirst();
		CreditCardName card = null;
		if(cardOptional.isPresent()) {
			
			card = cardOptional.get();
			
			CreditCardOffers offers = null;
			
			switch (location) {
			case "Hyderabad":
				offers = new HyderabadOffers();
				break;
				
			case "Delhi":
				offers = new DelhiOffers();	
				break;
			
			case "Bangalore":
				offers = new BangaloreOffers();
				break;	
			default:
				offers = new CreditCardOffers();
				break;
			}
			
			offers.getOffersByCardId(card.getCreditCardId());
			
			card.setCardOffers(offers);
			
		} else {
			throw new DataNotFound("Card not found for the cardId: "+ cardId);
		}
		
		return Utility.getResponseEntity(card, HttpStatus.FOUND, MessageConstants.DATA_FOUND);
	}
}
