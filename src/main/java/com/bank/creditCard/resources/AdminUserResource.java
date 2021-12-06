package com.bank.creditCard.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.exceptions.DataNotFound;
import com.bank.creditCard.exceptions.InvalidRequestException;
import com.bank.creditCard.services.AdminUserService;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;

/**
 * 
 * This is a resource class where the up-steam application requests Admin user
 * services like <br>
 * 1. View requests pending for Approval <br>
 * 2. View the details of request along with customer details<br>
 * 3. Admin user can Approve/Reject the request <br>
 *
 */
@RestController
@RequestMapping(path = "/admin")
public class AdminUserResource {
 @Autowired
 AdminUserService adminUserService;
 /**
  * This method will return the list of credit card requests waiting for
  * approval
  * @return List {@link CardRequestDetails} is binded in {@link ResponseEntity}
  * @throws InterruptedException When Async Service Thread is interrupted while
  *                              it's waiting, sleeping
  * @throws ExecutionException   When Async services are failed
  */
 @GetMapping("/all/requests")
 public ResponseEntity<ServiceResponse> getRequestPendingForApproval()
   throws InterruptedException, ExecutionException {
  CompletableFuture<Object> requestDetailsList = adminUserService
    .getRequestsPendingForApproval();
  return Utility.getResponseEntity(requestDetailsList.get(), HttpStatus.OK,
    MessageConstants.REQUEST_COMPLETED);
 }

 /**
  * This method is used to a request details along with Customer Details
  * @param requestId requestId of a {@link CardRequestDetails}
  * @return {@link CardRequestDetails} is binded in {@link ResponseEntity}
  * @throws InterruptedException When Async Service Thread is interrupted while
  *                              it's waiting, sleeping
  * @throws ExecutionException   When Async services are failed
  * @throws DataNotFound         When {@link CardRequestDetails} not for the
  *                              requestId
  */
 @GetMapping("/request/{requestId}")
 public ResponseEntity<ServiceResponse> getRequest(
   @PathVariable String requestId)
   throws InterruptedException, ExecutionException, DataNotFound {
  CompletableFuture<Object> requestDetails = adminUserService
    .getRequestDetails(requestId);
  return Utility.getResponseEntity(requestDetails.get(), HttpStatus.OK,
    MessageConstants.DATA_FOUND);
 }

 /**
  * This method is used to update the request with status type Approve/Reject
  * @param status    This is a path variable status type
  * @param requestId Request id of a request
  * @param message   Status message
  * @return {@link CardRequestDetails} with status message is binded in
  *         {@link ResponseEntity}
  * @throws DataNotFound            When requestId is not found in the system
  * @throws InvalidRequestException When status type is invalid
  */
 @PutMapping("/status/{status}/{requestId}")
 public ResponseEntity<ServiceResponse> approveOrRejectRequest(
   @PathVariable Short status, @PathVariable String requestId,
   @RequestParam(name = "message", required = false) String message)
   throws DataNotFound, InvalidRequestException {
  return adminUserService.approveOrRejectRequest(requestId, status, message);
 }
}
