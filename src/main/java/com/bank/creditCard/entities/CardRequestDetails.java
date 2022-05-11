package com.bank.creditCard.entities;

import java.time.Instant;

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
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "card_request_details")
public class CardRequestDetails {
    @Id
    private String requestId;
    private Long userId;
    private String creditCardId;
    private Short status;
    private Short requestType;
    private Long userCardId;
    private String message;

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
