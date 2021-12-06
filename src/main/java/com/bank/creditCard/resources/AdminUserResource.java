package com.bank.creditCard.resources;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin Resource", description = "APIs to which can used by Admin users to view/approve/reject Credit Card requests")
@RestController
@RequestMapping(path = "/admin")
public class AdminUserResource {
 @Autowired
 AdminUserService adminUserService;
 /**
  * This method will return the list of credit card requests waiting for
  * approval
  * @return List {@link CardRequestDetails} is bind in {@link ResponseEntity}
  * @throws InterruptedException When Async Service Thread is interrupted while
  *                              it's waiting, sleeping
  * @throws ExecutionException   When Async services are failed
  */
 @Operation (summary = "This method will return the list of credit card requests waiting for approval" )
 @ApiResponse(responseCode = "200 OK", description = MessageConstants.REQUEST_COMPLETED)
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
  * @return {@link CardRequestDetails} is bind in {@link ResponseEntity}
  * @throws InterruptedException When Async Service Thread is interrupted while
  *                              it's waiting, sleeping
  * @throws ExecutionException   When Async services are failed
  * @throws DataNotFound         When {@link CardRequestDetails} not for the
  *                              requestId
  */
 @Operation(summary = "This method is used to get credit card request details along with Customer Details")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "200 OK", description = MessageConstants.DATA_FOUND),
         @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND)})
 @GetMapping("/request/{requestId}")
 public ResponseEntity<ServiceResponse> getRequest(
    @Parameter(description = "RequestId id of a Credit Card request", required = true, example = "01d077e0-e42a-4628-badd-375a3ee5bdd1")
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
  * @return {@link CardRequestDetails} with status message is bind in
  *         {@link ResponseEntity}
  * @throws DataNotFound            When requestId is not found in the system
  * @throws InvalidRequestException When status type is invalid
  */
 @Operation(summary = "This method is used to update the request with status type Approve/Reject")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "202 Accepted", description = MessageConstants.STATUS_MESSAGE_REQUEST_APPROVED),
         @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND),
         @ApiResponse(responseCode = "400 Bad Request", description = "Invalid update status, please use {Approve : 1, Reject: -1 }"),
         @ApiResponse(responseCode = "400 Bad Request", description = "Cannot update request on credit card is generated")
 })
 @PutMapping("/status/{status}/{requestId}")
 public ResponseEntity<ServiceResponse> approveOrRejectRequest(
         @Parameter(description = "Approve/Reject status value", required = true, example = "1")
         @PathVariable Short status,
         @Parameter(description = "RequestId id of a Credit Card request", required = true, example = "01d077e0-e42a-4628-badd-375a3ee5bdd1")
         @PathVariable String requestId,
         @Parameter(description = "Status message", required = false)
   @RequestParam(name = "message", required = false) String message)
   throws DataNotFound, InvalidRequestException {
  return adminUserService.approveOrRejectRequest(requestId, status, message);
 }
}
