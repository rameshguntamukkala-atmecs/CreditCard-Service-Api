package com.bank.creditCard.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.exceptions.DataNotFound;
import com.bank.creditCard.exceptions.InvalidRequestException;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.repositories.CreditCardDetailsRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.utilities.Utility;
import com.bank.creditCard.validators.RequestValidator;

/**
 * 
 * This is Customer service layer where Customer related activities can be
 * performed
 *
 */
@Service
public class CreditCardUserService {
 @Autowired
 CustomerDetailsRepository customerDetailsRepository;
 @Autowired
 RequestValidator validator;
 @Autowired
 CreditCardDetailsRepository creditCardDetailsRepository;
 /**
  * This method is used to get the {@link CustomerDetails} from the DB
  * 
  * @param userId This is userId find the {@link CustomerDetails}
  * @return {@link CustomerDetails} is binded in {@link ResponseEntity}
  * @throws DataNotFound When not {@link CustomerDetails} found for the userId
  */
 public ResponseEntity<ServiceResponse> getUserDetails(Long userId)
   throws DataNotFound {
  Optional<CustomerDetails> customerDetails = customerDetailsRepository
    .findById(userId);

  if (customerDetails.isPresent()) {
   return Utility.getResponseEntity(customerDetails.get(), HttpStatus.FOUND,
     MessageConstants.DATA_FOUND);
  } else {
   throw new DataNotFound("User not found for the userId: " + userId);
  }

 }

 /**
  * This method is used to updated the Customer phone number, emailId etc.
  * 
  * @param userId This is the userId to find the {@link CustomerDetails}
  * @param customerDetails Updated customer fields of a {@link CustomerDetails}
  * @return Updated {@link CustomerDetails}
  * @throws DataNotFound When {@link CustomerDetails} is not found for userId
  * @throws InvalidRequestException When Customer primary fields (Like PAN
  *                                 number, FirstName, LastName) are updated
  */
 @Transactional
 public ResponseEntity<ServiceResponse> validateAndUpdateUserDetails(
   Long userId, CustomerDetails customerDetails)
   throws DataNotFound, InvalidRequestException {
  validator.userDetailsForUpdate(userId, customerDetails);
  customerDetailsRepository.save(customerDetails);
  return Utility.getResponseEntity(customerDetails, HttpStatus.CREATED,
    MessageConstants.USER_DETAILS_UPDATE_SUCCESS);
 }

 /**
  * This method is used to get all Credit Cards available for a Customer
  * 
  * @param userId UserId to find the {@link CustomerDetails}
  * @return List of {@link CustomerDetails} binded {@link ResponseEntity}
  */
 public ResponseEntity<ServiceResponse> getCardsAvailableForUser(Long userId) {
  List<CreditCardDetails> userCreditCardsList = creditCardDetailsRepository
    .findCardsByUserId(userId);
  return Utility.getResponseEntity(userCreditCardsList, HttpStatus.OK, null);
 }

 /**
  * This method is used to get a Credit Card details of a Customer
  * 
  * @param cardId cardId to find the {@link CreditCardDetails}
  * @return {@link CreditCardDetails} binded in {@link ResponseEntity}
  */
 public ResponseEntity<ServiceResponse> getCreditCardDetails(Long cardId) {
  Optional<CreditCardDetails> creditCardDetails = creditCardDetailsRepository
    .findById(cardId);

  if (creditCardDetails.isEmpty()) {
   return Utility.getResponseEntity(new DataNotFound(), HttpStatus.NOT_FOUND,
     MessageConstants.DATA_NOT_FOUND);
  }

  return Utility.getResponseEntity(creditCardDetails.get(), HttpStatus.FOUND,
    MessageConstants.DATA_FOUND);
 }
}
