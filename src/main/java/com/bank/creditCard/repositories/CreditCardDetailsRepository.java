package com.bank.creditCard.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.CreditCardDetails;

@Repository
public interface CreditCardDetailsRepository extends JpaRepository<CreditCardDetails, Long> {

    public CreditCardDetails findByCardNumber(Long cardNumber);
    List<CreditCardDetails> findByUserId(Long userId);
    List<CreditCardDetails> findByUserIdIn(List<Long> userIds);

}
