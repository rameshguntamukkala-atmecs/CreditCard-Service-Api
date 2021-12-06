package com.bank.creditCard.validators;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.DeficientException;
import com.bank.creditCard.Exceptions.DuplicateUserException;
import com.bank.creditCard.Exceptions.InvalidRequestException;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.io.entities.CreditCardRequestDetails;
import com.bank.creditCard.io.entities.TransactionInputDetails;
import com.bank.creditCard.repositories.CreditCardDetailsRepository;
import com.bank.creditCard.repositories.CreditCardNamesRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;

/**
 * This is a component which is used for validation for multiple requests.
 *
 */
@Component
public class RequestValidator {
 @Autowired
 CustomerDetailsRepository customerDetailsRepository;
 @Autowired
 CreditCardNamesRepository creditCardNamesRepository;
 @Autowired
 CreditCardDetailsRepository creditCardDetailsRepository;
 /**
  * This method is used to validate Credit Card Request Details
  * @param creditCardRequestDetails Credit Card Request details are binded in
  *                                 {@link CreditCardRequestDetails}
  * @throws DuplicateUserException When input PAN number already available in
  *                                system
  * @throws DeficientException     When user's salary is insufficient
  * @throws DataNotFound           When input card name is not found
  */
 public void validate(CreditCardRequestDetails creditCardRequestDetails)
   throws DuplicateUserException, DeficientException, DataNotFound {
  Optional<CustomerDetails> customerDetails = customerDetailsRepository
    .findCustomerDetailsByPanCardNumber(
      creditCardRequestDetails.getCustomerPanCardNumber());

  if (customerDetails.isPresent()) {
   throw new DuplicateUserException(
     "A customer is exist with this PAN CARD number ");
  }

  BigDecimal customerMinAnnualSalary = new BigDecimal("50000.00");

  if (customerMinAnnualSalary
    .compareTo(creditCardRequestDetails.getCustomerAnnualSalary()) > 0) {
   throw new DeficientException(
     "Customer Annual Salary must be greater than Rs. 50000");
  }

  Optional<CreditCardName> cardName = creditCardNamesRepository
    .findById(creditCardRequestDetails.getRequestedCardName());

  if (cardName.isPresent()) {
   creditCardRequestDetails.setCreditCardName(cardName.get());
  } else {
   throw new DataNotFound("Requested Card Details are not found",
     HttpStatus.NOT_FOUND);
  }

 }

 /**
  * This method is used to validate the Customer details with updated values
  * @param userId          userId of a {@link CustomerDetails}
  * @param customerDetails Updated values of a {@link CustomerDetails}
  * @throws DataNotFound            When no customer found for userId
  * @throws InvalidRequestException When Customer primary data value are not
  *                                 matched
  */
 public void userDetailsForUpdate(Long userId, CustomerDetails customerDetails)
   throws DataNotFound, InvalidRequestException {
  Optional<CustomerDetails> customerDetailsInDB = customerDetailsRepository
    .findById(userId);

  if (!customerDetailsInDB.isEmpty()) {
   boolean primaryDataIsChanged = false;

   if (!customerDetailsInDB.get().getUserId()
     .equals(customerDetails.getUserId())
     || !customerDetailsInDB.get().getFirstName()
       .equals(customerDetails.getFirstName())
     || !customerDetailsInDB.get().getLastName()
       .equals(customerDetails.getLastName())
     || !customerDetailsInDB.get().getPanCardNumber()
       .equals(customerDetails.getPanCardNumber())) {
    primaryDataIsChanged = true;
   }

   if (customerDetailsInDB.get().getUsername() != null && !customerDetailsInDB
     .get().getUsername().equals(customerDetails.getUsername())) {
    primaryDataIsChanged = true;
   }

   if (primaryDataIsChanged) {
    throw new InvalidRequestException("Primary user details cannot be updated");
   }

  } else {
   throw new DataNotFound(
     MessageConstants.CUSTOMER_DETAILS_NOT_FOUND + " " + userId);
  }

 }

 /**
  * This method is used to validate a Credit Card Transaction
  * @param transactionDetails Transaction input details are binded in
  *                           {@link TransactionInputDetails}
  */
 public void validateCreditCardTransaction(
   TransactionInputDetails transactionDetails) {
  CreditCardDetails creditCardDetails = creditCardDetailsRepository
    .findCardDetailsByCardNumber(transactionDetails.getCardNumber());

  if (creditCardDetails == null) {
   transactionDetails
     .setTransactionStatus(Constants.TRANSACTION_STATUS_FAILURE);
   transactionDetails
     .setTransactionMsg(MessageConstants.INVALID_CREDIT_CARD_DETAILS);
  } else {
   transactionDetails
     .setTransactionStatus(Constants.TRANSACTION_STATUS_SUCCESS);
   transactionDetails.setCreditCardDetails(creditCardDetails);

   if (!Utility.compareBothValuesMonthAndYearsAreEqual(
     transactionDetails.getCardValidTo(), creditCardDetails.getValidTo())) {
    transactionDetails
      .setTransactionStatus(Constants.TRANSACTION_STATUS_FAILURE);
    transactionDetails
      .setTransactionMsg(MessageConstants.INVALID_CREDIT_CARD_DETAILS);
   }

   if (!Utility.compareBothValues(transactionDetails.getCardCvv(),
     creditCardDetails.getCardVerificatonValue())
     && !Utility.compareBothValues(transactionDetails.getCardPin(),
       creditCardDetails.getCardPin())) {
    transactionDetails
      .setTransactionStatus(Constants.TRANSACTION_STATUS_FAILURE);
    transactionDetails
      .setTransactionMsg(MessageConstants.INVALID_CREDIT_CARD_DETAILS);
   }

   if (Constants.TRANSACTION_TYPE_DEBIT
     .equals(transactionDetails.getTransactionType())) {
    BigDecimal balanceAmount = null;

    if (creditCardDetails.getOutStandingAmount() != null) {
     balanceAmount = creditCardDetails.getCardLimit()
       .subtract(creditCardDetails.getOutStandingAmount());
    } else {
     balanceAmount = creditCardDetails.getCardLimit();
    }

    if ((balanceAmount.compareTo(transactionDetails.getAmount()) < 0)) {
     transactionDetails
       .setTransactionStatus(Constants.TRANSACTION_STATUS_FAILURE);
     transactionDetails
       .setTransactionMsg(MessageConstants.INSUFFICIENT_BALANCE);
    }

   }

  }

 }
}
