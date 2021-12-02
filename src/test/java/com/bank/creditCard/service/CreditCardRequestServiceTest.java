package com.bank.creditCard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.QueryTimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.bank.creditCard.Exceptions.CreditCardAppException;
import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.Exceptions.DeficientException;
import com.bank.creditCard.Exceptions.DuplicateUserException;
import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.io.entities.CreditCardRequestDetails;
import com.bank.creditCard.io.entities.ServiceResponse;
import com.bank.creditCard.repositories.CreditCardNamesRepository;
import com.bank.creditCard.repositories.CreditCardRequestRepository;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.services.CreditCardRequestService;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.EntityHelper;
import com.bank.creditCard.utilities.Utility;
import com.bank.creditCard.validators.RequestValidator;

@SpringBootTest
public class CreditCardRequestServiceTest {

	@InjectMocks
	private CreditCardRequestService service;
	
	@Mock
	CreditCardRequestRepository creditCardRequestRepository;
	
	@Mock
	CustomerDetailsRepository customerDetailsRepository;
	
	@Mock
	CreditCardNamesRepository creditCardNamesRepository;

	@Mock
	EntityHelper entityHelper;
	
	@Spy
	RequestValidator validator;
	
	
	
	
	/**
	 * Mock Data
	 */
	
	private List<CreditCardName> getMockCreditCardNameList() {
		
		List<CreditCardName>  cardsList = new ArrayList<>();
		cardsList.add(getMockCreditCardName("MONEYBACK", "MASTER"));
		cardsList.add(getMockCreditCardName("BUSINESS", "VISSA"));
		cardsList.add(getMockCreditCardName("GOLD", "MASTER"));
		
		
		return cardsList;
	}
	
	private CreditCardName getMockCreditCardName(String cardName, String cardType) {
		
		CreditCardName card = new CreditCardName();
		
		card.setCardName(cardName);
		card.setCreditCardDescription("Card Mock Description");
		card.setCreditCardId(cardName);
		card.setCreditCardValidityInMonths(Short.valueOf("24"));
		card.setCreditCardType(cardType);
		
		return card;
	}
	
	private Optional<CreditCardName> getMockCreditCardName() {
		return Optional.of(getMockCreditCardName("MONEYBACK", "MASTER"));
		
	}
	
	private CreditCardRequestDetails getMockCreditCardRequestDetails() {
		
		CreditCardRequestDetails creditCardRequestDetails = new CreditCardRequestDetails();
		
		creditCardRequestDetails.setCustomerFirstName("Ramesh");
		creditCardRequestDetails.setCustomerLastName("Kumar");
		creditCardRequestDetails.setCustomerAge(Short.valueOf("29"));
		creditCardRequestDetails.setCustomerAnnualSalary(BigDecimal.valueOf(260000));
		creditCardRequestDetails.setCustomerEmailId("ramesh@gmail.com");
		creditCardRequestDetails.setCustomerPanCardNumber("ADBCFERFS");
		creditCardRequestDetails.setRequestedCardName("MONEYBACK");
		
		return creditCardRequestDetails;
	}
	
	private CardRequestDetails getMockCustomerRequestDetails() {
		CardRequestDetails requestDetails = new CardRequestDetails();
		requestDetails.setRequestId(Utility.getUUID());
		requestDetails.setCreditCardId("MONEYBACK");
		return requestDetails;
	}

	private CustomerDetails getMockCustomerDetails() {
		CustomerDetails customerDetailsNew = new CustomerDetails();
		customerDetailsNew.setUserId(Long.valueOf("1"));
		customerDetailsNew.setFirstName("Ramesh");
		customerDetailsNew.setFirstName("Kumar");
		return customerDetailsNew;
	}
	
	private List<CardRequestDetails> getMockCardRequestDetailsList() {
		
		List<CardRequestDetails> requestDetailsList = new ArrayList<>();
		
		requestDetailsList.add(getMockCardRequestDetails(1l));
		requestDetailsList.add(getMockCardRequestDetails(2l));
		requestDetailsList.add(getMockCardRequestDetails(3l));
		
		return requestDetailsList;
	}
	
	private CardRequestDetails getMockCardRequestDetails(Long userId) {

		CardRequestDetails requestDetails = new CardRequestDetails();
		requestDetails.setRequestId(Utility.getUUID());
		requestDetails.setRequestType(Short.valueOf("1"));
		requestDetails.setStatus(Short.valueOf("1"));
		requestDetails.setUserId(userId);
		
		return requestDetails;
	}
	
	/**
	 * Mock Data  Ends
	 */
	
	/**
	 * Test case starts
	 */
	
	@BeforeEach
	public void init() {
		
		ReflectionTestUtils.setField(validator, "customerDetailsRepository", customerDetailsRepository);
		ReflectionTestUtils.setField(validator, "creditCardNamesRepository", creditCardNamesRepository);
	}
	
	@Test
	public void testFor_getAvaliableCards_positiveCase() {
		
		List<CreditCardName> expectedCardsList = getMockCreditCardNameList(); 
		
		Mockito.doReturn(expectedCardsList).when(creditCardNamesRepository).findAll();
		
		List<CreditCardName> actualCardsList = service.getAvaliableCards();
		
		assertEquals(expectedCardsList.size(), actualCardsList.size());
		
	}
	
	@Test
	public void testFor_getAvaliableCards_negativeCase() {
		
		Mockito.doThrow(QueryTimeoutException.class).when(creditCardNamesRepository).findAll();
		assertThrows(Exception.class, () -> {service.getAvaliableCards();});

	}
	
	
	@Test
	public void testFor_saveCreditCardRequestDetails_positiveCase() throws DuplicateUserException, DeficientException, DataNotFound {
		
		CreditCardRequestDetails creditCardRequestDetails =   getMockCreditCardRequestDetails();
		
		Optional<CustomerDetails> customerDetails = Optional.empty();
		Optional<CreditCardName> creditCardName = getMockCreditCardName();
		
		CustomerDetails customerDetailsNew = getMockCustomerDetails();
		
		CardRequestDetails requestDetails = getMockCustomerRequestDetails();
		
		Mockito.doReturn(customerDetails).when(customerDetailsRepository).findCustomerDetailsByPanCardNumber(anyString());
		Mockito.doReturn(creditCardName).when(creditCardNamesRepository).findById(creditCardRequestDetails.getRequestedCardName());
		Mockito.doReturn(customerDetailsNew).when(entityHelper).generatedCustomerDetails(creditCardRequestDetails);
		Mockito.doReturn(customerDetailsNew).when(customerDetailsRepository).save(any(CustomerDetails.class));
		Mockito.doReturn(requestDetails).when(entityHelper).generateCardRequestDetails(creditCardRequestDetails, customerDetailsNew);
		Mockito.doReturn(requestDetails).when(creditCardRequestRepository).save(any(CardRequestDetails.class));
		
		CardRequestDetails actualRequestDetails = service.saveCreditCardRequestDetails(creditCardRequestDetails);
		
		assertNotNull(actualRequestDetails);
		assertNotNull(actualRequestDetails.getRequestId());
		assertEquals(creditCardRequestDetails.getRequestedCardName(), actualRequestDetails.getCreditCardId());
		
		
	}

	@Test	
	public void testFor_saveCreditCardRequestDetails_panNumberIsNotValid_negativeCase() throws DuplicateUserException, DeficientException, DataNotFound {
		
		CreditCardRequestDetails creditCardRequestDetails =   getMockCreditCardRequestDetails();
		
		Optional<CustomerDetails> customerDetails = Optional.of(new CustomerDetails());
		
		Mockito.doReturn(customerDetails).when(customerDetailsRepository).findCustomerDetailsByPanCardNumber(creditCardRequestDetails.getCustomerPanCardNumber());
		
		assertThrows(CreditCardAppException.class, () -> {
			service.saveCreditCardRequestDetails(creditCardRequestDetails);
			});
		
	}
	
	@Test	
	public void testFor_saveCreditCardRequestDetails_whenSarlaryDataIsNotSatisfied_negativeCase() throws DuplicateUserException, DeficientException, DataNotFound {
		
		CreditCardRequestDetails creditCardRequestDetails =   getMockCreditCardRequestDetails();
		creditCardRequestDetails.setCustomerAnnualSalary(BigDecimal.valueOf(2000.00));
		Optional<CustomerDetails> customerDetails = Optional.empty();
		
		Mockito.doReturn(customerDetails).when(customerDetailsRepository).findCustomerDetailsByPanCardNumber(creditCardRequestDetails.getCustomerPanCardNumber());
		
		assertThrows(CreditCardAppException.class, () -> {
			service.saveCreditCardRequestDetails(creditCardRequestDetails);
			});
		
	}
	
	@Test	
	public void testFor_saveCreditCardRequestDetails_whenRequestedCardNameNotFound_negativeCase() throws DuplicateUserException, DeficientException, DataNotFound {
		
		CreditCardRequestDetails creditCardRequestDetails =   getMockCreditCardRequestDetails();

		Optional<CustomerDetails> customerDetails = Optional.empty();
		Optional<CreditCardName> creditCardName = Optional.empty();
		
		Mockito.doReturn(customerDetails).when(customerDetailsRepository).findCustomerDetailsByPanCardNumber(creditCardRequestDetails.getCustomerPanCardNumber());
		Mockito.doReturn(creditCardName).when(creditCardNamesRepository).findById(creditCardRequestDetails.getRequestedCardName());
		
		assertThrows(CreditCardAppException.class, () -> {
			service.saveCreditCardRequestDetails(creditCardRequestDetails);
			});
		
	}
	
	
	@Test
	public void testFor_getRequestDetailsByStatus_positiveCase() {
		
		List<CardRequestDetails> cardRequestDetails = getMockCardRequestDetailsList();
		Mockito.doReturn(cardRequestDetails).when(creditCardRequestRepository).findAllByStatus(anyShort());
		
		List<CardRequestDetails> expectedRequestDetails = service.getRequestDetailsByStatus(Short.valueOf("0"));
		assertEquals(cardRequestDetails.size(), expectedRequestDetails.size());
		
	}
	
	
	@Test
	public void testFor_getRequestDetailsByStatus_negativeCase() {
		
		Mockito.doThrow(QueryTimeoutException.class).when(creditCardRequestRepository).findAllByStatus(anyShort());
		assertThrows(Exception.class, () -> {service.getRequestDetailsByStatus(Short.valueOf("0"));});
		
	}
	
	@Test
	public void testFor_getRequestDetails_positiveCase() {
		Optional<CardRequestDetails> requestDetails = Optional.of(getMockCardRequestDetails(1l));
		Mockito.doReturn(requestDetails).when(creditCardRequestRepository).findById(requestDetails.get().getRequestId());
		
		Optional<CardRequestDetails> expectedRequestDetails = service.getRequestDetails(requestDetails.get().getRequestId());
		
		assertTrue(expectedRequestDetails.isPresent());
		assertEquals(requestDetails.get().getRequestId(), expectedRequestDetails.get().getRequestId());
		
	}
	
	@Test
	public void testFor_getRequestDetails_negativeCase() {
		String requestId = Utility.getUUID();
		Optional<CardRequestDetails> requestDetails = Optional.empty();
		Mockito.doReturn(requestDetails).when(creditCardRequestRepository).findById(requestId);
		
		Optional<CardRequestDetails> expectedRequestDetails = service.getRequestDetails(requestId);
		
		assertTrue(expectedRequestDetails.isEmpty());
		
	}

	@Test
	public void testFor_updateStatusToRequest_positiveCase() {
		String requestId = Utility.getUUID();
		String message = "Request Approved";
		Mockito.doNothing().when(creditCardRequestRepository).updateStatusToTheRequest(Constants.REQUEST_STATUS_APPROVED, requestId, message);
		
		service.updateStatusToRequest(Constants.REQUEST_STATUS_APPROVED, requestId, message);
		
		Mockito.verify(creditCardRequestRepository, times(1)).updateStatusToTheRequest(Constants.REQUEST_STATUS_APPROVED, requestId, message);
		
	}
	
	@Test
	public void testFor_getCardDetailsByLocation_WhenLocationIsHyderabad_positiveCase() throws DataNotFound {
		
		String cardId = "MONEYBACK";
		
		List<CreditCardName> cardNamesList = getMockCreditCardNameList();
		Optional<CreditCardName> creditCardName = getMockCreditCardName();
		
		Mockito.doReturn(cardNamesList).when(creditCardNamesRepository).findAll();
		
		ResponseEntity<ServiceResponse> response = service.getCardDetailsByLocation(cardId, "Hyderabad");
		
		assertEquals(creditCardName.get().getCardName(), ((CreditCardName) response.getBody().getResponseObject()).getCardName());
		
		
	}
	
	@Test
	public void testFor_getCardDetailsByLocation_WhenLocationIsBangalore_positiveCase() throws DataNotFound {
		
		String cardId = "MONEYBACK";
		
		List<CreditCardName> cardNamesList = getMockCreditCardNameList();
		Optional<CreditCardName> creditCardName = getMockCreditCardName();
		
		Mockito.doReturn(cardNamesList).when(creditCardNamesRepository).findAll();
		
		ResponseEntity<ServiceResponse> response = service.getCardDetailsByLocation(cardId, "Bangalore");
		
		assertEquals(creditCardName.get().getCardName(), ((CreditCardName) response.getBody().getResponseObject()).getCardName());
		assertNotNull(((CreditCardName) response.getBody().getResponseObject()).getCardOffers());
	}
	
	@Test
	public void testFor_getCardDetailsByLocation_WhenLocationIsDelhi_positiveCase() throws DataNotFound {
		
		String cardId = "MONEYBACK";
		
		List<CreditCardName> cardNamesList = getMockCreditCardNameList();
		Optional<CreditCardName> creditCardName = getMockCreditCardName();

		Mockito.doReturn(cardNamesList).when(creditCardNamesRepository).findAll();
		
		ResponseEntity<ServiceResponse> response = service.getCardDetailsByLocation(cardId, "Delhi");
		
		assertEquals(creditCardName.get().getCardName(), ((CreditCardName) response.getBody().getResponseObject()).getCardName());
		assertNotNull(((CreditCardName) response.getBody().getResponseObject()).getCardOffers());
	}
	
	@Test
	public void testFor_getCardDetailsByLocation_WhenLocationIsMumbai_positiveCase() throws DataNotFound {
		
		String cardId = "MONEYBACK";
		
		List<CreditCardName> cardNamesList = getMockCreditCardNameList();
		Optional<CreditCardName> creditCardName = getMockCreditCardName();
		
		Mockito.doReturn(cardNamesList).when(creditCardNamesRepository).findAll();
		
		ResponseEntity<ServiceResponse> response = service.getCardDetailsByLocation(cardId, "Mumbai");
		
		assertEquals(creditCardName.get().getCardName(), ((CreditCardName) response.getBody().getResponseObject()).getCardName());
		assertNotNull(((CreditCardName) response.getBody().getResponseObject()).getCardOffers());
	}
	
}
