package com.bank.creditCard.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.exceptions.DataNotFound;
import com.bank.creditCard.exceptions.DeficientException;
import com.bank.creditCard.exceptions.DuplicateUserException;
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

/**
 * 
 * This service class will interact with repository layer and perform business
 * activities like <br>
 * 1. Get Available Credit Cards from using repository 2. Get Individual Credit
 * Card details and its offer details 2. Validate new Credit Card request 3.
 * Save Credit Card Request and create Customer
 *
 */
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

	/**
	 * This method is used to validate the {@link CreditCardRequestDetails} and Save
	 * the {@link CardRequestDetails} along with creating a {@link CustomerDetails}
	 * 
	 * @param creditCardRequestDetails Credit Card request details are binded in
	 *                                 {@link CreditCardRequestDetails}
	 * @return {@link CardRequestDetails} with auto generated requestId
	 * @throws DuplicateUserException When input PAN number is already exist in DB
	 * @throws DeficientException     When Customer salary is insufficient to get a
	 *                                credit card
	 * @throws DataNotFound           When request {@link CreditCardName} is not
	 *                                found
	 */
	@Transactional
	public CardRequestDetails saveCreditCardRequestDetails(CreditCardRequestDetails creditCardRequestDetails)
			throws DuplicateUserException, DeficientException, DataNotFound {

		requestValidator.validate(creditCardRequestDetails);

		CustomerDetails customerDetails = entityHelper.generatedCustomerDetails(creditCardRequestDetails);

		customerDetailsRepository.save(customerDetails);

		CardRequestDetails cardRequestDetails = entityHelper.generateCardRequestDetails(creditCardRequestDetails,
				customerDetails);

		creditCardRequestRepository.save(cardRequestDetails);

		return cardRequestDetails;
	}

	/**
	 * This method will interacts with a repository to find
	 * {@link CardRequestDetails} which are in a particular status
	 * 
	 * @param status Credit Card request status id
	 * @return List of {@link CardRequestDetails} which are matched with status
	 */
	public List<CardRequestDetails> getRequestDetailsByStatus(Short status) {
		return creditCardRequestRepository.findAllByStatus(status);
	}

	/**
	 * This method will update the status of a credit card request with a statusId
	 * and status message
	 * 
	 * @param status    This is an updated status id
	 * @param requestId This is a credit card request requestId
	 * @param message   This is a status message explains the status.
	 */
	@Transactional
	public void updateStatusToRequest(Short status, String requestId, String message) {
		creditCardRequestRepository.updateStatusToTheRequest(status, requestId, message);
	}

	/**
	 * This method will filter a requested {@link CreditCardName} details and based
	 * on requested location details it identify the appropriate
	 * {@link CreditCardOffers} details object and bind to {@link CreditCardName}
	 * 
	 * @param cardId   cardId in {@link CreditCardName}
	 * @param location City name to identify the offers
	 * @return {@link CreditCardName} binded in {@link ResponseEntity}
	 * @throws DataNotFound When {@link CreditCardName} is not found for the
	 *                      requested cardId
	 */
	public ResponseEntity<ServiceResponse> getCardDetailsByLocation(String cardId, String location)
			throws DataNotFound {

		Optional<CreditCardName> cardOptional = creditCardNamesRepository.findAll().stream()
				.filter(card -> cardId.equals(card.getCreditCardId())).findFirst();
		CreditCardName card = null;
		if (cardOptional.isPresent()) {

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
			throw new DataNotFound("Card not found for the cardId: " + cardId);
		}
		return Utility.getResponseEntity(card, HttpStatus.FOUND, MessageConstants.DATA_FOUND);
	}
}
