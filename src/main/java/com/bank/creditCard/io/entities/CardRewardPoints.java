package com.bank.creditCard.io.entities;

import java.math.BigInteger;
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

@Getter
@Setter
@ToString
@Entity
@Table (name = "REWARD_POINTS_DETAILS")
public class CardRewardPoints {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (name = "CARD_ID")
	private Long cardId;
	
	@Column (name = "REWARD_POINTS")
	private BigInteger rewardPoints;
	
	@Column (name = "CLAIM_STATUS")
	private Short claimStatus;
	
	@Column (name = "CREATED_TIME")
	private Timestamp createdTime;
	
	@Column (name = "MODIFIED_TIME")
	private Timestamp modifiedTime;
	
}
