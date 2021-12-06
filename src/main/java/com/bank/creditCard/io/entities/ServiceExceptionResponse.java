package com.bank.creditCard.io.entities;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceExceptionResponse {
 private String responseMessage;
 private int responseCode;
 private String responseName;
 private ZonedDateTime responseTimstamp;
}
