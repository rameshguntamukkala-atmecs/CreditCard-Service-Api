package com.bank.creditCard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.exceptions.DataNotFound;
import com.bank.creditCard.exceptions.InvalidRequestException;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.repositories.CreditCardDetailsRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.services.CreditCardUserService;
import com.bank.creditCard.utilities.MessageConstants;
import com.bank.creditCard.validators.RequestValidator;

@SpringBootTest
public class CreditCardUserServiceTest {

 @InjectMocks
 CreditCardUserService service;
 @Mock
 CustomerDetailsRepository customerDetailsRepository;
 @Spy
 RequestValidator validator;
 @Mock
 CreditCardDetailsRepository creditCardDetailsRepository;
 
 public CustomerDetails getCustomerDetailsMockData() {
  CustomerDetails customerDetails = new CustomerDetails();
  customerDetails.setUserId(1l);
  customerDetails.setFirstName("Ramesh");
  customerDetails.setLastName("Kumar");
  customerDetails.setAge(Short.valueOf("30"));
  customerDetails.setPanCardNumber("ABCDEFGHIJ");
  return customerDetails;
 }
 
 public CreditCardDetails getCreditCardDetailsMockData(Long userId, Long cardNumber, String cvv, String pin, String nameOnCard) {
  CreditCardDetails cardDetails = new CreditCardDetails();
  cardDetails.setUserCardId(1l);
  cardDetails.setCardNumber(cardNumber);
  cardDetails.setCardPin(pin);
  cardDetails.setCardVerificatonValue(cvv);
  cardDetails.setCardName("MASTER CARD");
  cardDetails.setUserId(userId);
  return cardDetails;
 }
 
 public List<CreditCardDetails> getCreditCarDetailsList() {
  CreditCardDetails card1 = getCreditCardDetailsMockData(1l, 9987665422345578l, "123", "1234", "RameshKumar");
  List<CreditCardDetails> cardsList = new ArrayList<CreditCardDetails>();
  cardsList.add(card1);
  return cardsList;
 }
 
 @BeforeEach
 public void init() {
   
   ReflectionTestUtils.setField(validator, "customerDetailsRepository", customerDetailsRepository);
 }
 
 @Test
 public void test_getUserDetails_postiveCase() throws DataNotFound {
  Optional<CustomerDetails> customer = Optional.of(getCustomerDetailsMockData());
  Long userId = customer.get().getUserId();
  Mockito.doReturn(customer).when(customerDetailsRepository).findById(userId);
  ResponseEntity<ServiceResponse> response = service.getUserDetails(userId);
  assertEquals(HttpStatus.FOUND.value(), response.getBody().getResponseCode());
  assertEquals(customer.get().getUserId(), ((CustomerDetails)response.getBody().getResponseObject()).getUserId());
 }
 
 @Test
 public void test_getUserDetails_negativeCase() throws DataNotFound {
  Optional<CustomerDetails> customer = Optional.empty();
  Long userId = 1l;
  Mockito.doReturn(customer).when(customerDetailsRepository).findById(userId);
  assertThrows(DataNotFound.class, ()-> {service.getUserDetails(userId);});
 }
 
 @Test
 public void test_validateAndUpdateUserDetails_postiveCase() throws DataNotFound, InvalidRequestException {
  CustomerDetails customer = getCustomerDetailsMockData();
  customer.setPhoneNumber(998998878l);
  customer.setEmailId("rameshku.ar@gmail.com");
  Long userId = customer.getUserId();
  Optional<CustomerDetails> customerDetails = Optional.of(customer);
  Mockito.doReturn(customerDetails).when(customerDetailsRepository).findById(userId);
  Mockito.doReturn(customerDetails.get()).when(customerDetailsRepository).save(any(CustomerDetails.class));
  ResponseEntity<ServiceResponse> response = service.validateAndUpdateUserDetails(userId, customer);
  assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
  assertEquals(MessageConstants.USER_DETAILS_UPDATE_SUCCESS, response.getBody().getResponseMessage());
 }
 
 @Test
 public void test_validateAndUpdateUserDetails_whenPrimaryDataIsChanged_negativeCase() throws DataNotFound, InvalidRequestException {
  CustomerDetails customer = getCustomerDetailsMockData();
  customer.setUsername("rameshkumar");
  CustomerDetails updatedCustomer = getCustomerDetailsMockData();
  updatedCustomer.setPanCardNumber("HIJKLMNOPQ");
  updatedCustomer.setPhoneNumber(998998878l);
  updatedCustomer.setEmailId("rameshku.ar@gmail.com");
  updatedCustomer.setUsername("ramesh.kg");
  Long userId = updatedCustomer.getUserId();
  Optional<CustomerDetails> customerDetails = Optional.of(customer);
  Mockito.doReturn(customerDetails).when(customerDetailsRepository).findById(userId);
  assertThrows(InvalidRequestException.class, () -> {service.validateAndUpdateUserDetails(userId, updatedCustomer);});
 }
 
 @Test
 public void test_validateAndUpdateUserDetails_whenUserNotFound_negativeCase() throws DataNotFound, InvalidRequestException {
  CustomerDetails updatedCustomer = getCustomerDetailsMockData();
  updatedCustomer.setPanCardNumber("HIJKLMNOPQ");
  updatedCustomer.setPhoneNumber(998998878l);
  updatedCustomer.setEmailId("rameshku.ar@gmail.com");
  updatedCustomer.setUsername("ramesh.kg");
  Long userId = updatedCustomer.getUserId();
  Optional<CustomerDetails> customerDetails = Optional.empty();
  Mockito.doReturn(customerDetails).when(customerDetailsRepository).findById(userId);
  assertThrows(DataNotFound.class, () -> {service.validateAndUpdateUserDetails(userId, updatedCustomer);});
 }
 
 @Test
 public void test_getCardsAvailableForUser_postiveCase() {
  List<CreditCardDetails> cards = getCreditCarDetailsList();
  Mockito.doReturn(cards).when(creditCardDetailsRepository).findCardsByUserId(1l);
  ResponseEntity<ServiceResponse> response = service.getCardsAvailableForUser(1l);
  assertEquals(cards.size(), ((List)response.getBody().getResponseObject()).size());
 }
 
 @Test
 public void test_getCreditCardDetails_positiveCase() {
  Optional<CreditCardDetails> cardDetails = Optional.of(getCreditCardDetailsMockData(1l, 9987665422345578l, "123", "1234", "RameshKumar")); 
  Mockito.doReturn(cardDetails).when(creditCardDetailsRepository).findById(1l);
  ResponseEntity<ServiceResponse> response = service.getCreditCardDetails(1l);
  assertEquals(HttpStatus.FOUND, response.getStatusCode());
  assertEquals(cardDetails.get().getCardNumber(), ((CreditCardDetails)response.getBody().getResponseObject()).getCardNumber());
 }
 
 @Test
 public void test_getCreditCardDetails_neagativeCase() {
  Optional<CreditCardDetails> cardDetails = Optional.empty(); 
  Mockito.doReturn(cardDetails).when(creditCardDetailsRepository).findById(1l);
  ResponseEntity<ServiceResponse> response = service.getCreditCardDetails(1l);
  assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  assertEquals(DataNotFound.class, ((DataNotFound)response.getBody().getResponseObject()).getClass());
 }
}
