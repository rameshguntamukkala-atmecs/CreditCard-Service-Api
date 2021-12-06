package com.bank.creditCard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * This is a custom exception class raised when validation is failed due to
 * invalid data
 *
 */
@Getter
@Setter
public class InvalidRequestException extends CreditCardAppException {
 private static final long serialVersionUID = -6884407040697092516L;
 private String message;
 private HttpStatus status;
 public InvalidRequestException() {
  this.message = "Invalid request data";
  this.status = HttpStatus.BAD_REQUEST;
 }

 public InvalidRequestException(String message , Throwable cause) {
  this.message = message;
  this.status = HttpStatus.BAD_REQUEST;
 }

 public InvalidRequestException(String message , HttpStatus status) {
  this.message = message;
  this.status = status;
 }

 public InvalidRequestException(String message) {
  this.message = message;
  this.status = HttpStatus.BAD_REQUEST;
 }
}
