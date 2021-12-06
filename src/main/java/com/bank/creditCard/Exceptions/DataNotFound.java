package com.bank.creditCard.Exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * This is a custom exception class used when requested data not found in DB
 *
 */
@Getter
@Setter
public class DataNotFound extends CreditCardAppException {
 private static final long serialVersionUID = -2702574628749689581L;
 private String message;
 private HttpStatus status;
 public DataNotFound() {
  this.message = "No data is found for this request";
  this.status = HttpStatus.NOT_FOUND;
 }

 public DataNotFound(String message , Throwable cause) {
  this.message = message;
  this.status = HttpStatus.NOT_FOUND;
 }

 public DataNotFound(String message , HttpStatus status) {
  this.message = message;
  this.status = status;
 }

 public DataNotFound(String message) {
  this.message = message;
  this.status = HttpStatus.NOT_FOUND;
 }
}
