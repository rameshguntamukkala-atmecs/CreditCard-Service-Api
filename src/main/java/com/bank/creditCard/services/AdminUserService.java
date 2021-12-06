package com.bank.creditCard.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.exceptions.DataNotFound;
import com.bank.creditCard.exceptions.InvalidRequestException;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;

/**
 * This is an admin service layer where Admin business activities are performed
 *
 */
@Service
public class AdminUserService {
 private static final Logger logger = LoggerFactory
   .getLogger(AdminUserService.class);
 @Autowired
 CreditCardRequestService creditCardRequestService;
 @Autowired
 CustomerDetailsRepository customerDetailsRepository;
 /**
  * This method with interacts with {@link CreditCardRequestService} layer and
  * get all {@link CardRequestDetails} waiting for Approval<br>
  * <p>
  * This is an asynchronous method. When a resource module invoke this method,
  * it will run on separate thread and performs all activities in the method.
  * When multiple requests try to invoke this method, this method will run in
  * multiple threads asynchronously with out latency
  * </p>
  * @return It will return List of {@link CardRequestDetails} binded in
  *         {@link CompletableFuture}
  * @throws InterruptedException When Asynchronous thread is interrupted while
  *                              it's waiting, sleeping
  */
 @Async("asyncExecutor")
 public CompletableFuture<Object> getRequestsPendingForApproval()
   throws InterruptedException {
  logger.info("Thread start - {}", Thread.currentThread().getName());
  List<CardRequestDetails> requestDetailsList = creditCardRequestService
    .getRequestDetailsByStatus(Constants.REQUEST_STATUS_PROGRESS);
  Set<Long> customerUserIds = requestDetailsList.stream()
    .map(CardRequestDetails::getUserId).collect(Collectors.toSet());
  Map<Long, CustomerDetails> customerDetailsMap = customerDetailsRepository
    .findCustomerDetailsByUserIds(customerUserIds).stream()
    .collect(Collectors.toMap(CustomerDetails::getUserId, user -> user));
  requestDetailsList.forEach(request -> request
    .setCustomerDetails(customerDetailsMap.get(request.getUserId())));
  logger.info("Thread end - {}", Thread.currentThread().getName());
  return CompletableFuture.completedFuture(requestDetailsList);
 }

 /**
  * This method is used to Approve or Reject the {@link CardRequestDetails}
  * @param requestId Request Id in {@link CardRequestDetails}
  * @param status    Approve/Reject status id
  * @param message   Status message
  * @return On successful status update it will {@link CardRequestDetails} with
  *         updated status details
  * @throws DataNotFound            When no {@link CardRequestDetails} found for
  *                                 the requestId
  * @throws InvalidRequestException When an invalid status id is tried to
  *                                 updated for a {@link CardRequestDetails}
  */
 @Transactional
 public ResponseEntity<ServiceResponse> approveOrRejectRequest(String requestId,
   Short status, String message) throws DataNotFound, InvalidRequestException {

  if (!(Constants.REQUEST_STATUS_APPROVED.equals(status)
    || Constants.REQUEST_STATUS_REJECTED.equals(status))) {
   throw new InvalidRequestException(
     "Invalid update status, please use {Approve : 1, Reject: -1 }");
  }

  Optional<CardRequestDetails> cardRequestDetails = creditCardRequestService
    .getRequestDetails(requestId);

  if (cardRequestDetails.isPresent()) {

   if (Constants.REQUEST_STATUS_CARD_GENERATED
     .equals(cardRequestDetails.get().getStatus())) {
    throw new InvalidRequestException(
      "Cannot update request on credit card is generated. Invalid request!!");
   }

   creditCardRequestService.updateStatusToRequest(status, requestId, message);
   cardRequestDetails.get().setStatus(status);
   return Utility.getResponseEntity(cardRequestDetails.get(),
     HttpStatus.ACCEPTED, MessageConstants.STATUS_MESSAGE_REQUEST_APPROVED);
  } else {
   throw new DataNotFound("Could not find the requested Id in our system.");
  }

 }

 /**
  * This method used to get a {@link CardRequestDetails} along with the
  * {@link CustomerDetails}
  * @param requestId Request Id in {@link CardRequestDetails}
  * @return {@link CardRequestDetails} binded with {@link CustomerDetails}
  * @throws DataNotFound When {@link CardRequestDetails} are not for the
  *                      requestId
  */
 public CompletableFuture<Object> getRequestDetails(String requestId)
   throws DataNotFound {
  Optional<CardRequestDetails> requestDetails = creditCardRequestService
    .getRequestDetails(requestId);

  if (requestDetails.isPresent()) {
   Optional<CustomerDetails> customerDetails = customerDetailsRepository
     .findById(requestDetails.get().getUserId());

   if (customerDetails.isPresent()) {
    CustomerDetails details = customerDetails.get();
    requestDetails.get().setCustomerDetails(details);
   }

  } else {
   throw new DataNotFound();
  }

  return CompletableFuture.completedFuture(requestDetails.get());
 }
}
