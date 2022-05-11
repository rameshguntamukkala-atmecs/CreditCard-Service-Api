package com.bank.creditCard.scheduler;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.exceptions.DataNotFound;
import com.bank.creditCard.repositories.CreditCardDetailsRepository;
import com.bank.creditCard.repositories.CreditCardNamesRepository;
import com.bank.creditCard.repositories.CreditCardRequestRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.Utility;

/**
 * This is a service class used to generate new Credit Cards based on Customer
 * Request details
 */
@Component
public class GenerateCreditCardsService {
	@Autowired
	CreditCardRequestRepository cardRequestRepository;
	@Autowired
	CustomerDetailsRepository customerDetailsRepository;
	@Autowired
	CreditCardNamesRepository creditCardNamesRepository;
	@Autowired
	CreditCardDetailsRepository creditCardDetailsRepository;
	private static Logger logger = LoggerFactory.getLogger(GenerateCreditCardsService.class);

	/**
	 * This method will generate and save a new credit card on Customer
	 *
	 * @param customerDetailsList List of Customer details to filter the Actual
	 *                            Customer
	 * @param creditCardNamesList List of {@link CreditCardName} to get a
	 *                            {@link CreditCardName} details
	 * @param requestDetails      Customer request details
	 * @throws DataNotFound When required data not found
	 */
	@Async("asyncExecutor")
	@Transactional
	public void generateAndSaveCreditCard(List<CustomerDetails> customerDetailsList, List<CreditCardName> creditCardNamesList, CardRequestDetails requestDetails) throws DataNotFound {
		logger.info("Credit Card request: {}", requestDetails.getRequestId());
		CreditCardName creditCardName = creditCardNamesList.stream().filter(card -> card.getCreditCardId().equals(requestDetails.getCreditCardId())).findAny().orElseThrow(() -> new DataNotFound("CreditCardName data is not found: " + requestDetails.getCreditCardId()));
		CustomerDetails customerDetails = customerDetailsList.stream().filter(user -> user.getUserId().equals(requestDetails.getUserId())).findAny().orElseThrow(() -> new DataNotFound("Customer Data is not found: " + requestDetails.getUserId()));
		CreditCardDetails creditCardDetails = generatedCreditCardDetails(creditCardName, customerDetails);
		creditCardDetailsRepository.save(creditCardDetails);
		cardRequestRepository.updateUserCardIdAndStatusByRequestId(creditCardDetails.getUserCardId(), Constants.REQUEST_STATUS_CARD_GENERATED, requestDetails.getRequestId());
		logger.info("Credit Card is generated for the user: {}", customerDetails.getUserId());
	}

	/**
	 * This method will generate an {@link CreditCardDetails} entity based on
	 * {@link CreditCardName} and {@link CustomerDetails}
	 *
	 * @param creditCardName  {@link CreditCardName} customer requested
	 * @param customerDetails {@link CustomerDetails} who requested
	 * @return New {@link CreditCardDetails} entity
	 */
	private CreditCardDetails generatedCreditCardDetails(CreditCardName creditCardName, CustomerDetails customerDetails) {
		CreditCardDetails creditCardDetails = new CreditCardDetails();
		Long creditCardNumber = generatedCreditCardNumber();
		String cardPin = Utility.randomFourDigitNumber();
		String cvv = Utility.randomThreeDigitNumber();
		creditCardDetails.setCardName(creditCardName.getCreditCardName());
		creditCardDetails.setCardType(creditCardName.getCreditCardType());
		creditCardDetails.setCardNumber(creditCardNumber);
		creditCardDetails.setCardVerificationValue(cvv);
		creditCardDetails.setCardPin(cardPin);
		creditCardDetails.setValidFrom(new Date(System.currentTimeMillis()));
		creditCardDetails.setValidTo(Utility.getNewCardValidToDate(Optional.ofNullable(creditCardName.getValidityInMonths())));
		creditCardDetails.setCardStatus(Constants.CREDIT_CARD_STATUS_ACTIVE);
		creditCardDetails.setNameOnCard(customerDetails.getFirstName());
		creditCardDetails.setCardLimit(Utility.calculateCardLimitBy(customerDetails.getSalaryPerYear()));
		creditCardDetails.setUserId(customerDetails.getUserId());
		return creditCardDetails;
	}

	/**
	 * This method will generate a new credit card number
	 *
	 * @return A unique 16 digit credit card number
	 */
	private Long generatedCreditCardNumber() {
		String creditCardNumberStr = Utility.randomSixteenDigitNumber();
		Long creditCardNumber = Long.valueOf(creditCardNumberStr);
		CreditCardDetails creditCardDetails = creditCardDetailsRepository.findByCardNumber(creditCardNumber);

		if (creditCardDetails != null) {
			generatedCreditCardNumber();
		}

		return creditCardNumber;
	}

	/**
	 * This method to get List of customer details based on userIds
	 *
	 * @param customerUserIds Set of userIds to find {@link CustomerDetails}
	 * @return List for available {@link CustomerDetails}
	 */
	public List<CustomerDetails> getCustomerDetails(Set<Long> customerUserIds) {
		return customerDetailsRepository.findByUserIdIn(customerUserIds);
	}
}
