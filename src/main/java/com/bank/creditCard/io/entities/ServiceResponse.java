package com.bank.creditCard.io.entities;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class ServiceResponse {
 private final Object responseObject;
 private final String responseName;
 private final int responseCode;
 private final String responseMessage;
 private final ZonedDateTime responseTimestamp;
}
