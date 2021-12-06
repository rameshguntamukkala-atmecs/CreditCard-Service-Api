package com.bank.creditCard.schedular;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.entities.TransactionDetails;
import com.bank.creditCard.repositories.CreditCardDetailsRepository;
import com.bank.creditCard.repositories.CreditCardNamesRepository;
import com.bank.creditCard.repositories.CreditCardRequestRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.repositories.TransactionRepository;
import com.bank.creditCard.utilities.Constants;

/**
 * 
 * This a Scheduler service and schedulars starting point
 *
 */
@Component
public class SchedularService {
 @Autowired
 GenerateCreditCardsService generateCreditCardsService;
 @Autowired
 CreditCardRequestRepository cardRequestRepository;
 @Autowired
 CustomerDetailsRepository customerDetailsRepository;
 @Autowired
 CreditCardNamesRepository creditCardNamesRepository;
 @Autowired
 CreditCardDetailsRepository creditCardDetailsRepository;
 @Autowired
 TransactionRepository transactionRepository;
 @Autowired
 RewardPointsSchedularService rewardPointsSchedularService;
 private static Logger logger = LoggerFactory.getLogger(SchedularService.class);
 /**
  * This method will invoke every 10 minutes to process the Customer requests in
  * approve status
  */
 @Scheduled(cron = "${schedular.cron.expression.tenMinutes}")
 public void doTask() {
  logger.info("GenerateCreditCardsSchedularService - started");
  long startTime = System.currentTimeMillis();
  List<CardRequestDetails> cardRequestDetailsList = cardRequestRepository
    .findRequestDetailsByRequestTypeAndStatus(
      Constants.REQUEST_TYPE_NEW_CUSTOMER_NEW_CARD,
      Constants.REQUEST_STATUS_APPROVED);
  List<CreditCardName> creditCardNamesList = creditCardNamesRepository
    .findAll();
  List<CustomerDetails> customerDetailsList = null;

  if (cardRequestDetailsList != null && !cardRequestDetailsList.isEmpty()) {
   Set<Long> customerUserIds = cardRequestDetailsList.stream()
     .map(CardRequestDetails::getUserId).collect(Collectors.toSet());
   customerDetailsList = generateCreditCardsService
     .getCustomerDetails(customerUserIds);
   generateCreditCardsAndMapToUser(cardRequestDetailsList, customerDetailsList,
     creditCardNamesList);
  }

  logger.info("Total time: {} milliseconds",
    (System.currentTimeMillis() - startTime));
  logger.info("GenerateCreditCardsSchedularService - ended");
 }

 /**
  * This method will process all Customer requests to generate new Credit Cards
  * @param cardRequestDetailsList List for Credit Card request details
  * @param customerDetailsList    List of Customer details
  * @param creditCardNamesList    List of available credit card in system
  */
 private void generateCreditCardsAndMapToUser(
   List<CardRequestDetails> cardRequestDetailsList,
   List<CustomerDetails> customerDetailsList,
   List<CreditCardName> creditCardNamesList) {

  for (CardRequestDetails requestDetails: cardRequestDetailsList) {

   try {
    generateCreditCardsService.generateAndSaveCreditCard(customerDetailsList,
      creditCardNamesList, requestDetails);
   } catch (Exception e) {
    logger.error("Generate credit card is failed for requestId :"
      + requestDetails.getRequestId(), e);
   }

  }

 }

 /**
  * This method will invoke every 10 mins to process RewardPoints calculation
  * task for multiple transactions
  */
 @Scheduled(cron = "${schedular.cron.expression.tenMinutes}")
 public void rewardPointsSchedularTask() {
  logger.info("rewardPointsSchedularTask - started");
  List<TransactionDetails> transactionDetailsList = transactionRepository
    .findByRewardPointStatus(Constants.REWARD_POINT_STATUS);
  List<CreditCardName> creditCardNamesList = creditCardNamesRepository
    .findAll();

  if (transactionDetailsList != null && !transactionDetailsList.isEmpty()) {
   List<Long> creditCardIds = transactionDetailsList.stream()
     .map(TransactionDetails::getCardId).collect(Collectors.toList());
   List<CreditCardDetails> creditCardDetailsList = creditCardDetailsRepository
     .findAllById(creditCardIds);
   BigDecimal hundred = BigDecimal.valueOf(100.00);
   Map<Long, List<TransactionDetails>> tranctionDetailsMapByCardId = transactionDetailsList
     .stream().filter(trans -> (trans.getAmount().compareTo(hundred)) > -1)
     .collect(Collectors.groupingBy(TransactionDetails::getCardId));

   for (Map.Entry<Long, List<TransactionDetails>> entry: tranctionDetailsMapByCardId
     .entrySet()) {
    Long key = entry.getKey();
    List<TransactionDetails> transactions = entry.getValue();
    CreditCardDetails userCardDetails = creditCardDetailsList.stream()
      .filter(card -> key.equals(card.getUserCardId())).findAny().orElse(null);
    CreditCardName cardNameDetails = null;

    if (userCardDetails != null) {
     cardNameDetails = creditCardNamesList.stream()
       .filter(card -> userCardDetails.getCardName().equals(card.getCardName()))
       .findAny().orElse(null);
    }

    try {
     rewardPointsSchedularService.processTransactions(transactions,
       userCardDetails, cardNameDetails);
     logger.info("RewardPointPorcess completed for card: {}",
       userCardDetails.getUserCardId());
    } catch (Exception e) {
     logger.error("RewardPointPorcess failed: {}",
       userCardDetails.getUserCardId(), e);
    }

   }

  }

  logger.info("rewardPointsSchedularTask - ended");
 }

 /**
  * This method will invoke every 10 minutes to process the requests in reject
  * status and delete CustomerDetails from DB
  */
 @Scheduled(cron = "${schedular.cron.expression.tenMinutes}")
 public void deleteUserSchedularTask() {
  logger.info("deleteUserSchedularTask - started");
  List<CardRequestDetails> cardRequestDetailsList = cardRequestRepository
    .findAllByStatus(Constants.REQUEST_STATUS_REJECTED);
  List<Long> customerIds = cardRequestDetailsList.stream()
    .map(CardRequestDetails::getUserId).collect(Collectors.toList());

  if (!customerIds.isEmpty()) {
   List<CustomerDetails> customerDetails = customerDetailsRepository
     .findAllById(customerIds);
   List<CreditCardDetails> cardDetails = creditCardDetailsRepository
     .findCardsByUserIds(customerIds);
   Map<Long, List<CreditCardDetails>> cardsAndUserIdMap = cardDetails.stream()
     .collect(Collectors.groupingBy(CreditCardDetails::getUserId));
   List<CustomerDetails> customerToDelete = customerDetails.stream()
     .filter(user -> (null == cardsAndUserIdMap.get(user.getUserId())))
     .collect(Collectors.toList());
   customerDetailsRepository.deleteAll(customerToDelete);
  }

  logger.info("deleteUserSchedularTask - ended");
 }
}
