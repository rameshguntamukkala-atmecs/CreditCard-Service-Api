package com.bank.creditCard.utilities;

public class Constants {

	public static final Short REQUEST_TYPE_NEW_CUSTOMER_NEW_CARD = 1;
	public static final Short REQUEST_TYPE_NEW_CUSTOMER = 2;
	
	public static final Short REQUEST_STATUS_PROGRESS = 0;
	public static final Short REQUEST_STATUS_APPROVED = 1;
	public static final Short REQUEST_STATUS_CARD_GENERATED = 2;
	public static final Short REQUEST_STATUS_REJECTED = -1;
	public static final Short REQUEST_STATUS_CANCLED = -2;

	public static final Short CREDIT_CARD_STATUS_ACTIVE = 1;
	
	public static final Short TRANSACTION_STATUS_PENDING = 0;
	public static final Short TRANSACTION_STATUS_SUCCESS = 1;
	public static final Short TRANSACTION_STATUS_FAILURE = -1;
	public static final Short TRANSACTION_STATUS_CANCELED = -2;

	public static final Short TRANSACTION_TYPE_DEBIT = 1;
	public static final Short TRANSACTION_TYPE_CREDIT = 2;
	
	public static final Short REWARD_POINT_STATUS = 0;
	public static final Short REWARD_POINT_STATUS_NA = -1;
	public static final Short REWARD_POINT_STATUS_ADDED = 1;
	
	public static final Short TRANSACTION_CATEGORY_GROCERIES = 1;
	public static final Short TRANSACTION_CATEGORY_SHOPPING = 2;
	public static final Short TRANSACTION_CATEGORY_TRAVELLING = 3;
	public static final Short TRANSACTION_CATEGORY_RESTARENTS = 4;
	public static final Short TRANSACTION_CATEGORY_FUEL = 5;
	public static final Short TRANSACTION_CATEGORY_OTHER = 6;
	
}
