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

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.InvalidRequestException;
import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;

@Service
public class AdminUserService {

	private static final Logger logger = LoggerFactory.getLogger(AdminUserService.class);
	
	@Autowired
	CreditCardRequestService creditCardRequestService;
	
	@Autowired
	CustomerDetailsRepository customerDetailsRepository;
	
	@Async("asyncExecutor")
	public CompletableFuture<Object> getRequestsPendingForApproval() throws InterruptedException {
		logger.info("Thread start - {}", Thread.currentThread().getName());
		
		List<CardRequestDetails> requestDetailsList = creditCardRequestService.getRequestDetailsByStatus(Constants.REQUEST_STATUS_PROGRESS);
		
		Set<Long> customerUserIds = requestDetailsList.stream().map(CardRequestDetails::getUserId).collect(Collectors.toSet());
		
		Map<Long, CustomerDetails> customerDetailsMap = customerDetailsRepository.findCustomerDetailsByUserIds(customerUserIds).stream().collect(Collectors.toMap(CustomerDetails::getUserId, user -> user));
		
		requestDetailsList.forEach(request -> request.setCustomerDetails(customerDetailsMap.get(request.getUserId())));
		
		logger.info("Thread end - {}", Thread.currentThread().getName());
		
		return CompletableFuture.completedFuture(requestDetailsList);
	}

	@Transactional
	public ResponseEntity<ServiceResponse> approveOrRejectRequest(String requestId, Short status, String message) throws DataNotFound, InvalidRequestException {
		
		if(!(Constants.REQUEST_STATUS_APPROVED.equals(status) ||
				Constants.REQUEST_STATUS_REJECTED.equals(status)) )  {
			throw new InvalidRequestException("Invalid update status, please use {Approve : 1, Reject: -1 }");
		}
		
		Optional<CardRequestDetails> cardRequestDetails  = creditCardRequestService.getRequestDetails(requestId);
		
		if(cardRequestDetails.isPresent() ) {
			
			
			if(Constants.REQUEST_STATUS_CARD_GENERATED.equals(cardRequestDetails.get().getStatus())) {
				throw new InvalidRequestException("Cannot update request on credit card is generated. Invalid request!!");
			}
			
			creditCardRequestService.updateStatusToRequest(status, requestId, message);	
			cardRequestDetails.get().setStatus(status);
			
			return Utility.getResponseEntity(cardRequestDetails.get(), HttpStatus.ACCEPTED, MessageConstants.STATUS_MESSAGE_REQUEST_APPROVED);
			
		} else {
			throw new DataNotFound("Could not find the requested Id in our system.");
		}
		
		
	}

	public CompletableFuture<Object> getRequestDetails(String requestId) throws DataNotFound {
		
		Optional<CardRequestDetails> requestDetails = creditCardRequestService.getRequestDetails(requestId);
		
		if(requestDetails.isPresent()) {
			
			Optional<CustomerDetails> customerDetails = customerDetailsRepository.findById(requestDetails.get().getUserId());

			if(customerDetails.isPresent()) {
				CustomerDetails details = customerDetails.get();
				requestDetails.get().setCustomerDetails(details);
			} 
			
		} else {
			throw new DataNotFound();
		}
		
		return CompletableFuture.completedFuture(requestDetails.get());
	}

}
