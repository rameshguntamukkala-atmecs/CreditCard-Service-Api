package com.bank.creditCard.io.entities;

import java.math.BigDecimal;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.bank.creditCard.entities.CreditCardName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreditCardRequestDetails {

	@NotNull (message = "Customer First Name cannot be null")
	private String customerFirstName;
	
	@NotNull (message = "Customer Last Name cannot be null")
	private String customerLastName;

	@NotNull (message = "Customer Age cannot be null")
	@Min (value = 18, message = "Customer age must be between 18 and 70 years")
	@Max (value = 70, message = "Customer age must be between 18 and 70 years")
	private Short customerAge;
	
	@NotNull (message = "Customer Phone Number cannot be null")
	private Long customerPhoneNumber;
	
	@NotNull (message = "Customer Email Id cannot be null")
	@Email (message = "Customer Email Id is not valid")
	private String customerEmailId;
	
	@NotNull (message = "Customer Address cannot be null")
	private String customerAddress;
	
	@NotNull (message = "Customer Occupation cannot be null")
	private String customerOccupation;
	
	@NotNull (message = "Customer Designation cannot be null")
	private String customerDesignation;
	
	@NotNull (message = "Customer Annual Salary cannot be null")
	private BigDecimal customerAnnualSalary;
	
	@NotBlank (message = "Request Card Name cannot be null")
	private String requestedCardName;
	
	@NotNull (message = "Customer PAN CARD number cannot be null")
	@Size (min= 10, max = 10, message = "Customer PAN CARD is not valid")
	private String customerPanCardNumber; 
	
	private CreditCardName creditCardName;
	
}
