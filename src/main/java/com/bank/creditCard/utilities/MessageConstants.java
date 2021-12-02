package com.bank.creditCard.utilities;

import org.springframework.http.HttpStatus;

public class MessageConstants {

	public static final String CREDIT_CARD_REQUEST_CREATED_NEW_CUSTOMER = "Credit Card request is created for a new customer";
	public static final String REQUEST_COMPLETED = "Request completed successfully";
	public static final String DATA_FOUND = "The requested data is found";
	public static final String DATA_NOT_FOUND = "The requested data is not available";
	
	public static final String STATUS_MESSAGE_REQUEST_CREATED = "Your request is in progress";
	public static final String STATUS_MESSAGE_REQUEST_APPROVED = "Your request is in approved";
	public static final String STATUS_MESSAGE_REQUEST_CARD_GENERATED = "Your credit card is generated";
	public static final String STATUS_MESSAGE_REQUEST_REJECTED = "Your request got rejected by system";
	public static final String STATUS_MESSAGE_REQUEST_CANCELLED = "Your request got cancelled";
	public static final String STATUS_MESSAGE_NO_STATUS = "No status available";
	
	public static final String CUSTOMER_DETAILS_NOT_FOUND = "No customer details are found for the id: ";
	public static final String USER_DETAILS_UPDATE_SUCCESS = "User details are updated successfully";
	
	public static final String INVALID_CREDIT_CARD_DETAILS = "Credit card details are invalid";
	public static final String INSUFFICIENT_BALANCE = "Transaction decliend due to insufficient balance";
	public static final String TRANSACTION_COMPLETED = "Transaction is successfully completed";
	

}
