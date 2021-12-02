package com.bank.creditCard.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.CardRequestDetails;

@Repository
public interface CreditCardRequestRepository  extends JpaRepository<CardRequestDetails, String>{

	@Query(value = "SELECT * FROM CARD_REQUEST_DETAILS C WHERE C.REQUEST_TYPE=:requestType AND C.STATUS= :status ", nativeQuery = true)
	public List<CardRequestDetails> findRequestDetailsByRequestTypeAndStatus( @Param("requestType") Short requestType, @Param("status") Short status );

	@Modifying
	@Query(value = "UPDATE CARD_REQUEST_DETAILS SET MODIFIED_TIME = CURRENT_TIMESTAMP, USER_CARD_ID = :userCardId, STATUS = :status WHERE REQUEST_ID = :requestId", nativeQuery =  true)
	public void updateUserCardIdAndStatusToTheRequest(@Param("userCardId") Long userCardId, @Param("status") Short status, @Param("requestId") String requestId);

	@Query(value = "SELECT * FROM CARD_REQUEST_DETAILS C WHERE C.STATUS= :status ", nativeQuery = true)
	public List<CardRequestDetails> findAllByStatus(@Param("status") Short status);
	
	@Modifying
	@Query(value = "UPDATE CARD_REQUEST_DETAILS SET MODIFIED_TIME = CURRENT_TIMESTAMP, STATUS = :status, MESSAGE=:message WHERE REQUEST_ID = :requestId", nativeQuery =  true)
	public void updateStatusToTheRequest( @Param("status") Short status, @Param("requestId") String requestId,@Param("message") String message);
}
