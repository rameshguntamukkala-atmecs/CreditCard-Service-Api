package com.bank.creditCard.io.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;

import com.bank.creditCard.entities.CreditCardDetails;
import com.bank.creditCard.entities.CreditCardName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionInputDetails {
 @NotBlank
 private Long cardNumber;
 @NotBlank
 private Date cardValidTo;
 private String cardCvv;
 private String cardPin;
 @NotBlank
 private String nameOnCard;
 @NotBlank
 private BigDecimal amount;
 private String marchantName;
 @NotBlank
 private Short transactionType;
 private Short transactionCategory;
 private String transactionMsg;
 private String transactionPlace;
 private Short transactionStatus;
 private Timestamp transactionTime;
 @JsonIgnore
 private CreditCardDetails creditCardDetails;
 @JsonIgnore
 private CreditCardName creditCardName;
}
