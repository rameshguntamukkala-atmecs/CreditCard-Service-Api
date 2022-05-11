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
 * This is an entity class for table CUSTOMER_DETAILS
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_details")
public class CustomerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private Short age;
    private Long phoneNumber;
    private String occupationType;
    private String designation;
    private BigDecimal salaryPerYear;
    private String panCardNumber;
    private String username;
    private String address;
    private String emailId;

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
