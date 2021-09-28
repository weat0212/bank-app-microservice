package com.webcomm.creditcardservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.webcomm.creditcardservice.domain.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String>{

	List<CreditCard> findAll();

	@Query(value = "SELECT * FROM credit_card_case WHERE user_id = ?1", nativeQuery = true)
	List<CreditCard> findByUserId(String userId);
}
