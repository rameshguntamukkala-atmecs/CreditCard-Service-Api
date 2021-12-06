package com.bank.creditCard.utilities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.io.entities.CreditCardRequestDetails;
import com.bank.creditCard.io.entities.TransactionInputDetails;
import com.bank.creditCard.io.entities.TransactionSearchResponse;

/**
 * This is helper class which generate Entity objects
 *
 */
@Component
public class EntityHelper {
 /**
  * This method will generate {@link CustomerDetails} entity object based on
  * requestDetails
  * @param requestDetails Request details for a Credit Card
  * @return New {@link CustomerDetails} entity object
  */
 public CustomerDetails generatedCustomerDetails(
   CreditCardRequestDetails requestDetails) {
  CustomerDetails customerDetails = new CustomerDetails();
  customerDetails.setFirstName(requestDetails.getCustomerFirstName());
  customerDetails.setLastName(requestDetails.getCustomerLastName());
  customerDetails.setAge(requestDetails.getCustomerAge());
  customerDetails.setPhoneNumber(requestDetails.getCustomerPhoneNumber());
  customerDetails.setEmailId(requestDetails.getCustomerEmailId());
  customerDetails.setOccupationType(requestDetails.getCustomerOccupation());
  customerDetails.setDesignation(requestDetails.getCustomerDesignation());
  customerDetails.setAddress(requestDetails.getCustomerAddress());
  customerDetails.setSalaryPerYear(requestDetails.getCustomerAnnualSalary());
  customerDetails.setPanCardNumber(requestDetails.getCustomerPanCardNumber());
  customerDetails.setCreatedTime(new Timestamp(System.currentTimeMillis()));
  return customerDetails;
 }

 /**
  * This method is used to generate {@link CreditCardRequestDetails} entity
  * object
  * @param creditCardRequestDetails Request details for a Credit Card
  * @param customerDetails          {@link CustomerDetails} saved for a request
  * @return New {@link CreditCardRequestDetails} entity object
  */
 public CardRequestDetails generateCardRequestDetails(
   CreditCardRequestDetails creditCardRequestDetails,
   CustomerDetails customerDetails) {
  CardRequestDetails cardRequestDetails = new CardRequestDetails();
  cardRequestDetails.setRequestId(Utility.getUUID());
  cardRequestDetails
    .setRequestType(Constants.REQUEST_TYPE_NEW_CUSTOMER_NEW_CARD);
  cardRequestDetails.setStatus(Constants.REQUEST_STATUS_PROGRESS);
  cardRequestDetails.setUserId(customerDetails.getUserId());
  cardRequestDetails.setCreditCardId(
    creditCardRequestDetails.getCreditCardName().getCreditCardId());
  cardRequestDetails.setCreatedTime(new Timestamp(System.currentTimeMillis()));
  cardRequestDetails.setModifiedTime(new Timestamp(System.currentTimeMillis()));
  return cardRequestDetails;
 }

 /**
  * This method will generate {@link TransactionDetails} entity object
  * @param inputTransaction Transaction details submitted over a card
  * @return New {@link TransactionDetails} entity object
  */
 public TransactionDetails generateTransactionDetails(
   TransactionInputDetails inputTransaction) {
  TransactionDetails transactionDetails = new TransactionDetails();
  transactionDetails.setAmount(inputTransaction.getAmount());
  transactionDetails.setCardId((inputTransaction.getCreditCardDetails() != null)
    ? inputTransaction.getCreditCardDetails().getUserCardId()
    : null);
  transactionDetails.setCardNumber(inputTransaction.getCardNumber());
  transactionDetails.setTransactionType(inputTransaction.getTransactionType());
  transactionDetails
    .setTransactionCategory(inputTransaction.getTransactionCategory());
  transactionDetails
    .setTransactionStatus((inputTransaction.getTransactionStatus() != null)
      ? inputTransaction.getTransactionStatus()
      : Constants.TRANSACTION_STATUS_PENDING);
  transactionDetails
    .setTransactionPlace(inputTransaction.getTransactionPlace());
  transactionDetails.setMarchantName(inputTransaction.getMarchantName());
  transactionDetails
    .setTransactionTime(new Timestamp(System.currentTimeMillis()));
  transactionDetails
    .setCreatedTime(inputTransaction.getTransactionTime() != null
      ? new Timestamp(System.currentTimeMillis())
      : inputTransaction.getTransactionTime());
  transactionDetails.setModifiedTime(new Timestamp(System.currentTimeMillis()));
  return transactionDetails;
 }

 /**
  * This method is used to update the {@link TransactionDetails} with its
  * transaction status
  * @param inputTransaction   Transaction details after validation
  * @param transactionDetails updated {@link TransactionDetails} with its
  *                           transaction status and modified timestamp
  */
 public void generateTransactionDetailsWithStatus(
   TransactionInputDetails inputTransaction,
   TransactionDetails transactionDetails) {
  transactionDetails.setCardId((inputTransaction.getCreditCardDetails() != null)
    ? inputTransaction.getCreditCardDetails().getUserCardId()
    : null);
  transactionDetails.setCardNumber(inputTransaction.getCardNumber());
  transactionDetails
    .setTransactionStatus((inputTransaction.getTransactionStatus() != null)
      ? inputTransaction.getTransactionStatus()
      : Constants.TRANSACTION_STATUS_PENDING);
  transactionDetails
    .setTransactionTime(new Timestamp(System.currentTimeMillis()));
  transactionDetails
    .setCreatedTime(inputTransaction.getTransactionTime() != null
      ? new Timestamp(System.currentTimeMillis())
      : inputTransaction.getTransactionTime());
  transactionDetails.setModifiedTime(new Timestamp(System.currentTimeMillis()));

  if (!Constants.TRANSACTION_STATUS_FAILURE
    .equals(transactionDetails.getTransactionStatus())) {
   transactionDetails.setRewardPointsStatus(Constants.REWARD_POINT_STATUS);
  }

 }

 /**
  * This method will generate a {@link TransactionSearchResponse} model for
  * searched results.
  * @param transactionDetails List of {@link TransactionDetails} found as per
  *                           search criteria
  * @param cardDetails        {@link CreditCardDetails} of a customer to
  *                           calculate Outstanding amount on a card
  * @return {@link TransactionSearchResponse} binded with List of
  *         {@link TransactionDetails}
  */
 public TransactionSearchResponse generateTransactionSearchResponse(
   List<TransactionDetails> transactionDetails, CreditCardDetails cardDetails) {
  TransactionSearchResponse response = new TransactionSearchResponse();
  response.setTransactionDetails(transactionDetails);
  response.setCardNo(cardDetails.getCardNumber());
  response.setNameOnCard(cardDetails.getNameOnCard());
  response.setValidFrom(cardDetails.getValidFrom());
  response.setValidTo(cardDetails.getValidTo());
  response.setOutstandingAmount(cardDetails.getOutStandingAmount());
  BigDecimal totalAmount = null;

  if (transactionDetails != null && !transactionDetails.isEmpty()) {
   BigDecimal creditAmount = transactionDetails.stream()
     .filter(trans -> Constants.TRANSACTION_TYPE_CREDIT
       .equals(trans.getTransactionType()))
     .map(TransactionDetails::getAmount)
     .reduce(BigDecimal.ZERO, BigDecimal::add);
   BigDecimal debitAmount = transactionDetails.stream()
     .filter(trans -> Constants.TRANSACTION_TYPE_DEBIT
       .equals(trans.getTransactionType()))
     .map(TransactionDetails::getAmount)
     .reduce(BigDecimal.ZERO, BigDecimal::add);
   totalAmount = debitAmount.subtract(creditAmount);
   response.setTotalAmount(totalAmount);
  }

  return response;
 }
}
