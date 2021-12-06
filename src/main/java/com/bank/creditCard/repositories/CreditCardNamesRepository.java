package com.bank.creditCard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.CreditCardName;

@Repository
public interface CreditCardNamesRepository
  extends
   JpaRepository<CreditCardName, String> {
}
