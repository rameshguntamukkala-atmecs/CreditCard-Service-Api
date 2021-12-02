package com.bank.creditCard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.bank.creditCard.Exceptions.DataNotFound;
import com.bank.creditCard.entities.CardRequestDetails;
import com.bank.creditCard.entities.CustomerDetails;
import com.bank.creditCard.repositories.CustomerDetailsRepository;
import com.bank.creditCard.services.AdminUserService;
import com.bank.creditCard.services.CreditCardRequestService;
import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.Utility;

@SpringBootTest
public class AdminUserServiceTest {

	@InjectMocks
	AdminUserService service;
	
	@Mock
	CreditCardRequestService creditCardRequestService;
	
	@Mock
	CustomerDetailsRepository customerDetailsRepository;
	
	
	private CardRequestDetails getMockCustomerRequestDetails(Long userId) {
		CardRequestDetails requestDetails = new CardRequestDetails();
		requestDetails.setRequestId(Utility.getUUID());
		requestDetails.setRequestType(Short.valueOf("1"));
		requestDetails.setStatus(Short.valueOf("0"));
		requestDetails.setUserId(userId);
		
		return requestDetails;
	}

	private CustomerDetails getMockCustomerDetails(Long userId, String firstName, String lastName) {
		CustomerDetails customerDetailsNew = new CustomerDetails();
		customerDetailsNew.setUserId(userId);
		customerDetailsNew.setFirstName(firstName);
		customerDetailsNew.setFirstName(lastName);
		return customerDetailsNew;
	}
	
	private List<CustomerDetails> getCustomersList() {
		
		CustomerDetails customer1 = getMockCustomerDetails(1l, "Ramesh", "Kumar");
		CustomerDetails customer2 = getMockCustomerDetails(2l, "Harish", "Yanapu");
		CustomerDetails customer3 = getMockCustomerDetails(3l, "Ganesh", "Kumar");
		
		List<CustomerDetails> customerDetailsList = Arrays.asList(customer1, customer2, customer3);
		return customerDetailsList;
	}

	private List<CardRequestDetails> getCustomerRequestDetails() {
		List<CardRequestDetails> customerRequestsList = new ArrayList<>();
		
		CardRequestDetails request1 =  getMockCustomerRequestDetails(1l);
		CardRequestDetails request2 =  getMockCustomerRequestDetails(2l);
		CardRequestDetails request3 =  getMockCustomerRequestDetails(3l);
		
		CustomerDetails customer1 = getMockCustomerDetails(request1.getUserId(), "Ramesh", "Kumar");
		CustomerDetails customer2 = getMockCustomerDetails(request3.getUserId(), "Harish", "Yanapu");
		CustomerDetails customer3 = getMockCustomerDetails(request2.getUserId(), "Ganesh", "Kumar");
		
		request1.setCustomerDetails(customer1);
		request2.setCustomerDetails(customer2);
		request3.setCustomerDetails(customer3);
		
		customerRequestsList.add(request1);
		customerRequestsList.add(request3);
		customerRequestsList.add(request2);
		
		return customerRequestsList;
	}
	
	@Test
	public void test_getRequestsPendingForApproval_positiveCase() throws InterruptedException, ExecutionException  {
		List<CardRequestDetails> requestList = getCustomerRequestDetails();
		Set<Long> customerUserIds = requestList.stream().map(CardRequestDetails::getUserId).collect(Collectors.toSet());
		List<CustomerDetails> customerList = getCustomersList();
		
		Mockito.doReturn(requestList).when(creditCardRequestService).getRequestDetailsByStatus(Constants.REQUEST_STATUS_PROGRESS);
		Mockito.doReturn(customerList).when(customerDetailsRepository).findCustomerDetailsByUserIds(customerUserIds);
		
		CompletableFuture<Object> actualResponse = service.getRequestsPendingForApproval();
		
		assertTrue(actualResponse.isDone());
		List<CardRequestDetails> actualRequestList = (List<CardRequestDetails>) actualResponse.get();
		assertEquals(requestList.size(), actualRequestList.size());
		
		
	}
	
	@Test
	public void test_getRequestDetails_positiveCase() throws InterruptedException, ExecutionException, DataNotFound {
		
		Optional<CardRequestDetails> mockResultRequest =  Optional.of(getMockCustomerRequestDetails(1l)) ;
		Optional<CustomerDetails> mockResultcustomer = Optional.of(getMockCustomerDetails(1l, "Ramesh", "Kumar"));
		
		Mockito.doReturn(mockResultRequest).when(creditCardRequestService).getRequestDetails(mockResultRequest.get().getRequestId());
		Mockito.doReturn(mockResultcustomer).when(customerDetailsRepository).findById(mockResultRequest.get().getUserId());

		CompletableFuture<Object> actualResponse = service.getRequestDetails(mockResultRequest.get().getRequestId());
		
		assertTrue(actualResponse.isDone());
		CardRequestDetails actualResultRequest =  (CardRequestDetails) actualResponse.get();
		assertEquals(mockResultRequest.get().getRequestId(), actualResultRequest.getRequestId());
	}
	
	@Test
	public void test_getRequestDetails_neagativeCase() throws InterruptedException, ExecutionException, DataNotFound {
		String requestId = Utility.getUUID(); 
		Optional<CardRequestDetails> mockResultRequest =  Optional.empty();
		
		Mockito.doReturn(mockResultRequest).when(creditCardRequestService).getRequestDetails(requestId);

		assertThrows(DataNotFound.class, () -> {service.getRequestDetails(requestId);} );
		
		
	}
	
	
}
