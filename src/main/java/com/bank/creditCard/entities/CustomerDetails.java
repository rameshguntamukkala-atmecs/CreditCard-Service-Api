package com.bank.creditCard.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
 * 
 * This is an entity class for table CUSTOMER_DETAILS
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "CUSTOMER_DETAILS")
public class CustomerDetails {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long userId;
 @Column(name = "FIRST_NAME")
 private String firstName;
 @Column(name = "LAST_NAME")
 private String lastName;
 @Column(name = "AGE")
 private Short age;
 @Column(name = "PHONE_NUMBER")
 private Long phoneNumber;
 @Column(name = "OCCUPATION_TYPE")
 private String occupationType;
 @Column(name = "DESIGNATION")
 private String designation;
 @Column(name = "SALARY_PER_YEAR")
 private BigDecimal salaryPerYear;
 @Column(name = "PAN_CARD_NUMBER")
 private String panCardNumber;
 @Column(name = "CREATED_TIME")
 private Timestamp createdTime;
 @Column(name = "USERNAME")
 private String username;
 @Column(name = "ADDRESS")
 private String address;
 @Column(name = "EMAIL_ID")
 private String emailId;
}
