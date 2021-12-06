package com.bank.creditCard.Exceptions;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * This is a custom exception class used when Credit Card Transaction is failed
 *
 */
@Getter
public class CardDeclinedException extends Exception {
 private static final long serialVersionUID = -5484469653670340499L;
 private final String message;
 private final Integer httpCode;
 private final String httpValue;
 public CardDeclinedException() {
  super();
  this.message = "Credit card details are invalid";
  this.httpCode = HttpStatus.BAD_REQUEST.value();
  this.httpValue = HttpStatus.BAD_REQUEST.name();
 }

 public CardDeclinedException(String message , Throwable cause) {
  super(message, cause);
  this.message = message;
  this.httpCode = HttpStatus.BAD_REQUEST.value();
  this.httpValue = HttpStatus.BAD_REQUEST.name();
 }

 public CardDeclinedException(String message) {
  super(message);
  this.message = message;
  this.httpCode = HttpStatus.BAD_REQUEST.value();
  this.httpValue = HttpStatus.BAD_REQUEST.name();
 }
}
