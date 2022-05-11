package com.bank.creditCard.entities;

import java.math.BigDecimal;
import java.sql.Date;
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
 * This is an entity class for table USER_CREDIT_CARD_DETAILS
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_credit_card_details")
public class CreditCardDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCardId;
    private Long userId;
    private String cardName;
    private String cardType;
    private Long cardNumber;
    private Date validFrom;
    private Date validTo;
    private String cardVerificationValue;
    private String cardPin;
    private String nameOnCard;
    private BigDecimal cardLimit;
    private Short cardStatus;
    private BigDecimal outStandingAmount;
    private Timestamp outStandingAmountUpdateTs;

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
