package com.bank.creditCard.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.TransactionDetails;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetails, Long> {

    List<TransactionDetails> findByRewardPointsStatus(Short rewardPointsStatus);

 @Transactional
 @Modifying
 @Query("update TransactionDetails t set t.rewardPointsStatus = ?1 where t.transactionId in ?2")
 void updateRewardPointsStatusByTransactionIdIn(Short rewardPointsStatus, List<Long> transactionIds);

}
