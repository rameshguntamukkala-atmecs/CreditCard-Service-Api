package com.bank.creditCard.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * This is a custom exception class used when User already exist in system
 *
 */
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

 public DuplicateUserException(String message , HttpStatus status) {
  super(message, status);
  this.message = message;
  this.status = status;
 }

 public DuplicateUserException(String message , Throwable cause) {
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
