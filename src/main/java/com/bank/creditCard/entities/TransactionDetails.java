package com.bank.creditCard.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This is an entity class for table CREDIT_CARD_TRANSACTION_DETAILS
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "credit_card_transaction_details")
public class TransactionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Long cardId;
    private Long cardNumber;
    private BigDecimal transactionAmount;
    private Short transactionType;
    private Short transactionStatus;
    private String transactionStatusMsg;
    private Short transactionCategory;
    private String merchantName;
    private String transactionPlace;
    private Timestamp transactionTime;
    private Short rewardPointsStatus;

    @Column(
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
    )
    private Instant createTs;

    @Column(
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    )
    private Instant updateTs;
}
