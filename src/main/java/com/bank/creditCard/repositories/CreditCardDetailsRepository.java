package com.bank.creditCard.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.CreditCardDetails;

@Repository
public interface CreditCardDetailsRepository extends JpaRepository<CreditCardDetails, Long>{

	@Query(value ="SELECT * FROM USER_CREDIT_CARD_DETAILS U WHERE U.CARD_NUMBER= :cardNumber", nativeQuery =  true )
	public CreditCardDetails findCardDetailsByCardNumber(@Param("cardNumber") Long cardNumber);

	@Query(value = "SELECT * FROM USER_CREDIT_CARD_DETAILS WHERE USER_ID = :userId ", nativeQuery = true)
	public List<CreditCardDetails> findCardsByUserId(@Param("userId") Long userId);

	@Query(value = "SELECT * FROM USER_CREDIT_CARD_DETAILS WHERE USER_ID IN (:userIds) ", nativeQuery = true)
	public List<CreditCardDetails> findCardsByUserIds(@Param("userIds") List<Long> userIds);
	
}
