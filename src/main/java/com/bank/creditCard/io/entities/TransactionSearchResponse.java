package com.bank.creditCard.io.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.bank.creditCard.entities.TransactionDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionSearchResponse {
 private Long cardNo;
 private Date validFrom;
 private Date validTo;
 private String nameOnCard;
 private BigDecimal outstandingAmount;
 private BigDecimal totalAmount;
 List<TransactionDetails> transactionDetails;
}
