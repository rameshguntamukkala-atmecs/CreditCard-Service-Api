package com.bank.creditCard.schedular;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.repositories.CreditCardDetailsRepository;
import com.bank.creditCard.repositories.CreditCardNamesRepository;
import com.bank.creditCard.repositories.CreditCardRequestRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.Utility;

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
 private static Logger logger = LoggerFactory
   .getLogger(GenerateCreditCardsService.class);
 @Async("asyncExecutor")
 @Transactional
 public void generateAndSaveCreditCard(
   List<CustomerDetails> customerDetailsList,
   List<CreditCardName> creditCardNamesList, CardRequestDetails requestDetails)
   throws DataNotFound {
  logger.info("Credit Card request: {}", requestDetails.getRequestId());
  CreditCardName creditCardName = creditCardNamesList.stream()
    .filter(
      card -> card.getCreditCardId().equals(requestDetails.getCreditCardId()))
    .findAny().orElseThrow(() -> new DataNotFound(
      "CreditCardName data is not found: " + requestDetails.getCreditCardId()));
  CustomerDetails customerDetails = customerDetailsList.stream()
    .filter(user -> user.getUserId().equals(requestDetails.getUserId()))
    .findAny().orElseThrow(() -> new DataNotFound(
      "Customer Data is not found: " + requestDetails.getUserId()));
  CreditCardDetails creditCardDetails = generatedCreditCardDetails(
    creditCardName, customerDetails);
  creditCardDetailsRepository.save(creditCardDetails);
  cardRequestRepository.updateUserCardIdAndStatusToTheRequest(
    creditCardDetails.getUserCardId(), Constants.REQUEST_STATUS_CARD_GENERATED,
    requestDetails.getRequestId());
  logger.info("Credit Card is generated for the user: {}",
    customerDetails.getUserId());
 }

 private CreditCardDetails generatedCreditCardDetails(
   CreditCardName creditCardName, CustomerDetails customerDetails) {
  CreditCardDetails creditCardDetails = new CreditCardDetails();
  Long creditCardNumber = generatedCreditCardNumber();
  String cardPin = Utility.randomFourDigitNumber();
  String cvv = Utility.randomThreeDigitNumber();
  creditCardDetails.setCardName(creditCardName.getCardName());
  creditCardDetails.setCardType(creditCardName.getCreditCardType());
  creditCardDetails.setCardNumber(creditCardNumber);
  creditCardDetails.setCardVerificatonValue(cvv);
  creditCardDetails.setCardPin(cardPin);
  creditCardDetails.setValidFrom(new Date(System.currentTimeMillis()));
  creditCardDetails.setValidTo(Utility.getNewCardValidToDate(
    Optional.ofNullable(creditCardName.getCreditCardValidityInMonths())));
  creditCardDetails.setCardStatus(Constants.CREDIT_CARD_STATUS_ACTIVE);
  creditCardDetails.setNameOnCard(customerDetails.getFirstName());
  creditCardDetails.setCardLimit(
    Utility.calculateCardLimitBy(customerDetails.getSalaryPerYear()));
  creditCardDetails.setCreatedTime(new Timestamp(System.currentTimeMillis()));
  creditCardDetails.setModifiedTime(new Timestamp(System.currentTimeMillis()));
  creditCardDetails.setUserId(customerDetails.getUserId());
  return creditCardDetails;
 }

 private Long generatedCreditCardNumber() {
  String creditCardNumberStr = Utility.randomSixteenDigitNumber();
  Long creditCardNumber = Long.valueOf(creditCardNumberStr);
  CreditCardDetails creditCardDetails = creditCardDetailsRepository
    .findCardDetailsByCardNumber(creditCardNumber);

  if (creditCardDetails != null) {
   generatedCreditCardNumber();
  }

  return creditCardNumber;
 }

 public List<CustomerDetails> getCustomerDetails(Set<Long> customerUserIds) {
  return customerDetailsRepository
    .findCustomerDetailsByUserIds(customerUserIds);
 }
}
