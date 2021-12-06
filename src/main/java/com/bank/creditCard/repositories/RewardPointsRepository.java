package com.bank.creditCard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.CardRewardPoints;

@Repository
public interface RewardPointsRepository
  extends
   JpaRepository<CardRewardPoints, Long> {
 @Query(value = "SELECT * FROM REWARD_POINTS_DETAILS WHERE CARD_ID = :cardId", nativeQuery = true)
 public Optional<CardRewardPoints> findByCardId(@Param("cardId") Long cardId);
}
