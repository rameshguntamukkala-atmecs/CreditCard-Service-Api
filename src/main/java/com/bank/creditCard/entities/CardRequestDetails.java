package com.bank.creditCard.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bank.creditCard.utilities.Constants;
import com.bank.creditCard.utilities.MessageConstants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This is an entity class for table CARD_REQUEST_DETAILS
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "CARD_REQUEST_DETAILS")
public class CardRequestDetails {
 @Id
 private String requestId;
 @Column(name = "USER_ID")
 private Long userId;
 @Column(name = "CREDIT_CARD_ID")
 private String creditCardId;
 @Column(name = "STATUS")
 private Short status;
 @Column(name = "REQUEST_TYPE")
 private Short requestType;
 @Column(name = "CREATED_TIME")
 private Timestamp createdTime;
 @Column(name = "MODIFIED_TIME")
 private Timestamp modifiedTime;
 @Column(name = "USER_CARD_ID")
 private Long userCardId;
 @Column(name = "MESSAGE")
 private String message;
 @Transient
 @Getter(value = AccessLevel.NONE)
 private String statusMessage;
 public String getStatusMessage() {
  String theStatusMessage = null;

  if (Constants.REQUEST_STATUS_PROGRESS.equals(this.status)) {
   theStatusMessage = MessageConstants.STATUS_MESSAGE_REQUEST_CREATED;
  } else if (Constants.REQUEST_STATUS_APPROVED.equals(this.status)) {
   theStatusMessage = MessageConstants.STATUS_MESSAGE_REQUEST_APPROVED;
  } else if (Constants.REQUEST_STATUS_CARD_GENERATED.equals(this.status)) {
   theStatusMessage = MessageConstants.STATUS_MESSAGE_REQUEST_CARD_GENERATED;
  } else if (Constants.REQUEST_STATUS_REJECTED.equals(this.status)) {
   theStatusMessage = MessageConstants.STATUS_MESSAGE_REQUEST_REJECTED;
  } else if (Constants.REQUEST_STATUS_CANCLED.equals(this.status)) {
   theStatusMessage = MessageConstants.STATUS_MESSAGE_REQUEST_CANCELLED;
  } else {
   theStatusMessage = MessageConstants.STATUS_MESSAGE_NO_STATUS;
  }

  return theStatusMessage;
 }
 @Transient
 private CustomerDetails customerDetails;
}
