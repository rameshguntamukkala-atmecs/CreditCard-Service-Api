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
 * This is an Entity class for table CREDIT_CARD_BILLING_DETAILS
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "credit_card_billing_details")
public class CardBillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billingId;
    private Long cardId;
    private Date billingDate;
    private Date paymentDueDate;
    private Date nextBillingDate;
    private BigDecimal billingAmount;
    private BigDecimal minPaymentAmount;
    private Short paymentStatus;


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
