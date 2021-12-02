package com.bank.creditCard.Exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.creditCard.io.entities.ServiceExceptionResponse;

@RestControllerAdvice
public class CreditCardAppExceptionHandler {

	@ExceptionHandler (value = {CreditCardAppException.class})
	public ResponseEntity<Object> handleCreditCardAppExceptions(CreditCardAppException e) {
		
		ServiceExceptionResponse serviceExceptionResponse = new ServiceExceptionResponse(e.getMessage(), 
				e.getStatus().value(), 
				e.getStatus().name(), 
				ZonedDateTime.now(ZoneId.of("Z") ));
		
		return new ResponseEntity<>(serviceExceptionResponse, e.getStatus());
		
	}
}
