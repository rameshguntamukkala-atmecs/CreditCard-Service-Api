package com.bank.creditCard.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.TransactionDetails;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetails, Long> {

	@Query(value = "SELECT * FROM CREDIT_CARD_TRANSACTION_DETAILS WHERE REWARD_POINT_STATUS = :rewardPointsStatus AND TRANSACTION_STATUS = 1", nativeQuery = true)
	public List<TransactionDetails> findByRewardPointStatus(@Param("rewardPointsStatus") Short rewardPointStatus);

	@Modifying
	@Query(value = "UPDATE CREDIT_CARD_TRANSACTION_DETAILS SET REWARD_POINT_STATUS =:status WHERE TRANSACTION_ID IN (:transactionIds)", nativeQuery = true)
	void updateRewardPointStaus(@Param("status") Short status, @Param ("transactionIds") List<Long> transactionIds);


}
