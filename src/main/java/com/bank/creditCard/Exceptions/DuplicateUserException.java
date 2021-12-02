package com.bank.creditCard.Exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateUserException extends CreditCardAppException {

	private static final long serialVersionUID = 8357005655136537297L;

	private String message;
	private HttpStatus status;
	
	public DuplicateUserException() {
		super();
		this.message = "This PAN number is alreay used";
		this.status = HttpStatus.IM_USED;
	}
	public DuplicateUserException(String message, HttpStatus status) {
		super(message, status);
		this.message = message;
		this.status = status;
	}
	public DuplicateUserException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.status = HttpStatus.IM_USED;
	}
	public DuplicateUserException(String message) {
		super(message);
		this.message = message;
		this.status = HttpStatus.IM_USED;
	}
	
	
	

}
