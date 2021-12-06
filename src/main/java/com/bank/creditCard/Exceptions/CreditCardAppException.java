package com.bank.creditCard.Exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * This is a custom exception class and parent class for other custom exception
 *
 */
@Data
@AllArgsConstructor
public class CreditCardAppException extends Exception {
 private static final long serialVersionUID = -4372372259366950088L;
 private String message;
 private HttpStatus status;
 public CreditCardAppException(String message , Throwable cause) {
  super(message, cause);
  this.message = message;
  this.status = HttpStatus.INTERNAL_SERVER_ERROR;
 }

 public CreditCardAppException(String message) {
  super(message);
  this.message = message;
  this.status = HttpStatus.INTERNAL_SERVER_ERROR;
 }

 public CreditCardAppException() {
  super();
  this.message = "Credit card service internal exception";
  this.status = HttpStatus.INTERNAL_SERVER_ERROR;
 }
}
