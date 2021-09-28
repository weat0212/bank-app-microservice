package com.webcomm.creditcardservice.service;

import java.util.List;
import java.util.Optional;

import com.webcomm.creditcardservice.domain.CreditCard;

public interface CreditCardService{
	
	Optional<CreditCard> findByCaseNumber(String caseNumber);
	void save(CreditCard creditCard);

	List<CreditCard> findAllUserCases(String userId);
}
