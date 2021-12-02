package com.bank.creditCard.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//@Entity
//@Table(name = "USER_DETAILS")
public class UserDetails {

	/*
	 * @Id private String username;
	 * 
	 * @Column(name = "TOKEN") private String token;
	 * 
	 * @Column(name = "ACTIVE") private Short active;
	 * 
	 * @Column(name = "J_TOKEN") private String jToken;
	 * 
	 * @Column(name = "CREATED_TIME") private Timestamp createdTime;
	 * 
	 * @Column(name = "MODIFIED_TIME") private Timestamp modifiedTime;
	 */
	
	private String username;
	private String token;
	private Short active;
	private String jToken;
	private Timestamp createdTime;
	private Timestamp modifiedTime;

}
