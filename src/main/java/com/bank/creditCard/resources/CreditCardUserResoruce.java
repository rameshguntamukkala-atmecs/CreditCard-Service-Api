package com.bank.creditCard.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.InvalidRequestException;
import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CustomerDetails;
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
 @GetMapping("/details/{userId}")
 public ResponseEntity<ServiceResponse> getUserDetails(
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
 @PutMapping("/update/details/{userId}")
 public ResponseEntity<ServiceResponse> updateUserDetails(
   @PathVariable Long userId, @RequestBody CustomerDetails customerDetails)
   throws DataNotFound, InvalidRequestException {
  return service.validateAndUpdateUserDetails(userId, customerDetails);
 }

 /**
  * This method is used to get the all credit cards available for a Customer.
  * @param userId userId of a Customer
  * @return List of {@link CreditCardDetails} are binded to
  *         {@link ResponseEntity}
  */
 @GetMapping("/cards/{userId}")
 public ResponseEntity<ServiceResponse> getCardsAvailableForUser(
   @PathVariable Long userId) {
  return service.getCardsAvailableForUser(userId);
 }

 /**
  * This method is used to get a credit card details of a customer
  * @param cardId cardId of a {@link CreditCardDetails}
  * @return {@link CreditCardDetails} is binded to a {@link ResponseEntity}
  */
 @GetMapping("/card/details/{cardId}")
 public ResponseEntity<ServiceResponse> getCreditCardDetails(
   @PathVariable Long cardId) {
  return service.getCreditCardDetails(cardId);
 }
}
