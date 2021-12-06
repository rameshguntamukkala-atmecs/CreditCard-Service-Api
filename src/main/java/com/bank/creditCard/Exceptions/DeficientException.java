package com.bank.creditCard.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * This is a custom exception class raised in failure of validation due to
 * insufficient data
 *
 */
@Getter
@Setter
public class DeficientException extends CreditCardAppException {
 private static final long serialVersionUID = -4976636151613236004L;
 private String message;
 private HttpStatus status;
 public DeficientException() {
  this.message = "Provided data is insufficient";
  this.status = HttpStatus.PARTIAL_CONTENT;
 }

 public DeficientException(String message , Throwable cause) {
  super(message, cause);
  this.message = message;
  this.status = HttpStatus.PARTIAL_CONTENT;
 }

 public DeficientException(String message) {
  super(message);
  this.message = message;
  this.status = HttpStatus.PARTIAL_CONTENT;
 }

 public DeficientException(String message , HttpStatus status) {
  super(message);
  this.message = message;
  this.status = status;
 }
}
