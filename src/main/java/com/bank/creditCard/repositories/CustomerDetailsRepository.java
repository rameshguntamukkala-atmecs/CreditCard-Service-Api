package com.bank.creditCard.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.creditCard.entities.CustomerDetails;

@Repository
public interface CustomerDetailsRepository
  extends
   JpaRepository<CustomerDetails, Long> {
 @Query(value = "SELECT * FROM CUSTOMER_DETAILS WHERE PAN_CARD_NUMBER = :panCardNumber ", nativeQuery = true)
 public Optional<CustomerDetails> findCustomerDetailsByPanCardNumber(
   @Param("panCardNumber") String panCardNumber);
 @Query(value = "SELECT * FROM CUSTOMER_DETAILS WHERE USER_ID IN :userIds ", nativeQuery = true)
 public List<CustomerDetails> findCustomerDetailsByUserIds(
   @Param("userIds") Set<Long> customerUserIds);
}
