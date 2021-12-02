package com.bank.creditCard.resources;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.InvalidRequestException;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.services.AdminUserService;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;

@RestController
@RequestMapping(path = "/admin")
public class AdminUserResource {

	@Autowired
	AdminUserService adminUserService;

	/**
	 * Get request which are pending for Approval
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GetMapping("/all/requests")
	public ResponseEntity<ServiceResponse> getRequestPendingForApproval() throws InterruptedException, ExecutionException{
		
		CompletableFuture<Object> requestDetailsList = adminUserService.getRequestsPendingForApproval();
		
		return Utility.getResponseEntity(requestDetailsList.get(), HttpStatus.OK, MessageConstants.REQUEST_COMPLETED);
	}
	
	/**
	 * Get request details with user details
	 * @param requestId
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws DataNotFound
	 */
	@GetMapping("/request/{requestId}")
	public ResponseEntity<ServiceResponse> getRequest(@PathVariable String requestId) throws InterruptedException, ExecutionException, DataNotFound{
		
		CompletableFuture<Object> requestDetails = adminUserService.getRequestDetails(requestId);
		
		return Utility.getResponseEntity(requestDetails.get(), HttpStatus.OK, MessageConstants.DATA_FOUND);
	}
	
	/**
	 * Update the request by approve or reject
	 * @param status
	 * @param requestId
	 * @param message
	 * @return
	 * @throws DataNotFound
	 * @throws InvalidRequestException
	 */
	@PutMapping("/status/{status}/{requestId}")
	public ResponseEntity<ServiceResponse> approveOrRejectRequest(@PathVariable Short status,
			@PathVariable String requestId,
			@RequestParam(name = "message", required = false) String message) throws DataNotFound, InvalidRequestException{
		return adminUserService.approveOrRejectRequest(requestId, status, message);
		
	}
		
	
}
