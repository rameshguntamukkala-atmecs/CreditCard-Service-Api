package com.bank.creditCard.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.CardRequestDetails;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CreditCardRequestRepository extends JpaRepository<CardRequestDetails, String> {

	List<CardRequestDetails> findByRequestTypeAndStatus(Short requestType, Short status);

	@Transactional
	@Modifying
	@Query("update CardRequestDetails c set c.userCardId = ?1, c.status = ?2 where c.requestId = ?3")
	void updateUserCardIdAndStatusByRequestId(Long userCardId, Short status, String requestId);

	List<CardRequestDetails> findByStatus(Short status);

	@Transactional
	@Modifying
	@Query("update CardRequestDetails c set c.status = ?1, c.message = ?2 where c.requestId = ?3")
	int updateStatusAndMessageByRequestId(Short status, String message, String requestId);


//	@Modifying
//	@Query(value = "UPDATE CARD_REQUEST_DETAILS SET MODIFIED_TIME = CURRENT_TIMESTAMP, USER_CARD_ID = :userCardId, STATUS = :status WHERE REQUEST_ID = :requestId", nativeQuery = true)
//	public void updateUserCardIdAndStatusByRequestId(
//			@Param("userCardId") Long userCardId, @Param("status") Short status,
//			@Param("requestId") String requestId);


//	@Modifying
//	@Query(value = "UPDATE CARD_REQUEST_DETAILS SET MODIFIED_TIME = CURRENT_TIMESTAMP, STATUS = :status, MESSAGE=:message WHERE REQUEST_ID = :requestId", nativeQuery = true)
//	public void updateStatusAndMessageByRequestId(@Param("status") Short status,
//	                                              @Param("requestId") String requestId, @Param("message") String message);
}
