package com.webcomm.creditcardservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.creditcardservice.domain.CreditCard;
import com.webcomm.creditcardservice.repository.CreditCardRepository;

@Service
public class CreditCardServiceImpl implements CreditCardService {
	
	@Autowired
	private CreditCardRepository creditCardRepository;

	@Override
	public Optional<CreditCard> findByCaseNumber(String caseNumber) {
		return creditCardRepository.findById(caseNumber);
	}

	@Override
	public void save(CreditCard creditCard) {
		creditCardRepository.save(creditCard);
	}

	@Override
	public List<CreditCard> findAllUserCases(String userId) {
		return creditCardRepository.findByUserId(userId);
	}
}
