package com.bank.creditCard.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.creditCard.io.entities.ServiceExceptionResponse;

/**
 * 
 * This is configuration class to handle Custom exception
 *
 */
@RestControllerAdvice
public class CreditCardAppExceptionHandler {
 /**
  * This method will catch the custom exception serialize the exception to
  * {@link ServiceExceptionResponse}
  * @param e The custom exception raised in functionality
  * @return Returns a {@link ServiceExceptionResponse} binded in
  *         {@link ResponseEntity}
  */
 @ExceptionHandler(value = {CreditCardAppException.class})
 public ResponseEntity<Object> handleCreditCardAppExceptions(
   CreditCardAppException e) {
  ServiceExceptionResponse serviceExceptionResponse = new ServiceExceptionResponse(
    e.getMessage(), e.getStatus().value(), e.getStatus().name(),
    ZonedDateTime.now(ZoneId.of("Z")));
  return new ResponseEntity<>(serviceExceptionResponse, e.getStatus());
 }
}
