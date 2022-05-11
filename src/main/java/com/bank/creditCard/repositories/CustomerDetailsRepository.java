package com.bank.creditCard.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.CustomerDetails;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {
    Optional<CustomerDetails> findByPanCardNumberLike(String panCardNumber);
    List<CustomerDetails> findByUserIdIn(Set<Long> userIds);
}
