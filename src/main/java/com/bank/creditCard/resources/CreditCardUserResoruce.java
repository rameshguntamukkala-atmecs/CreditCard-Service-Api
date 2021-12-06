package com.bank.creditCard.resources;

import com.bank.creditCard.utilities.MessageConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.exceptions.DataNotFound;
import com.bank.creditCard.exceptions.InvalidRequestException;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.services.CreditCardUserService;

/**
 * 
 * This is a resource class where up-steam application request for credit card
 * user request services like <br>
 * 1. Get a Customer details <br>
 * 2. Update Customer details<br>
 * 3. View available credit cards for a Customer <br>
 * 4. View a credit cards details for a Customer <br>
 *
 */
@Tag(name = "User Resource", description = "APIs which are used to view/update Customer details and view Credit Card Details")
@RestController
@RequestMapping(path = "/user")
public class CreditCardUserResoruce {
 @Autowired
 CreditCardUserService service;
 /**
  * This method is used to get the Customer details
  * @param userId userId of a Customer
  * @return {@link CustomerDetails} is binded in {@link ResponseEntity}
  * @throws DataNotFound When Customer is not found for userId
  */
 @Operation(summary = "This method is used to get the Customer details")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "302 Found", description = MessageConstants.DATA_FOUND),
         @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND)})
 @GetMapping("/details/{userId}")
 public ResponseEntity<ServiceResponse> getUserDetails(
         @Parameter(description = "userId of a Customer", required = true, example = "10")
         @PathVariable Long userId) throws DataNotFound {
  return service.getUserDetails(userId);
 }

 /**
  * This method is used to update customer details like phone number, emailId
  * @param userId          userId of a Customer
  * @param customerDetails {@link RequestBody} serialize to
  *                        {@link CustomerDetails}
  * @return updated {@link CustomerDetails} binded in {@link ResponseEntity}
  * @throws DataNotFound            When Customer is not found for userId
  * @throws InvalidRequestException When Primary data like FirstName, LastName,
  *                                 PAN Number are updated.
  */
 @Operation(summary = "This method is used to update Customer details")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "201 Created", description = MessageConstants.USER_DETAILS_UPDATE_SUCCESS),
         @ApiResponse(responseCode = "400 Bad Request", description = "Primary user details cannot be updated"),
         @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND)})
 @PutMapping("/update/details/{userId}")
 public ResponseEntity<ServiceResponse> updateUserDetails(
         @Parameter(description = "userId of a Customer", required = true)
         @PathVariable Long userId,
         @Parameter(description = "Updated Customer details", required = true,
                 schema = @Schema(implementation = CustomerDetails.class))
         @RequestBody CustomerDetails customerDetails)
   throws DataNotFound, InvalidRequestException {
  return service.validateAndUpdateUserDetails(userId, customerDetails);
 }

 /**
  * This method is used to get the all credit cards available for a Customer.
  * @param userId userId of a Customer
  * @return List of {@link CreditCardDetails} are binded to
  *         {@link ResponseEntity}
  */
 @Operation(summary = "This method is used to get all Credit Cards for a Customer")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "200 OK")})
 @GetMapping("/cards/{userId}")
 public ResponseEntity<ServiceResponse> getCardsAvailableForUser(
         @Parameter(description = "userId of a Customer", required = true)
         @PathVariable Long userId) {
  return service.getCardsAvailableForUser(userId);
 }

 /**
  * This method is used to get a credit card details of a customer
  * @param cardId cardId of a {@link CreditCardDetails}
  * @return {@link CreditCardDetails} is binded to a {@link ResponseEntity}
  */
 @Operation(summary = "This method is used to get a Credit Card details")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "302 Found", description = MessageConstants.DATA_FOUND),
         @ApiResponse(responseCode = "404 Not Found", description = MessageConstants.DATA_NOT_FOUND)})
 @GetMapping("/card/details/{cardId}")
 public ResponseEntity<ServiceResponse> getCreditCardDetails(
    @Parameter(description = "CardId of a user Credit Card", required = true, example = "10")
         @PathVariable Long cardId) {
  return service.getCreditCardDetails(cardId);
 }
}
