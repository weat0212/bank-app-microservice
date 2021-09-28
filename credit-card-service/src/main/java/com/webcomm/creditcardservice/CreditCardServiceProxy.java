package com.webcomm.creditcardservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.webcomm.creditcardservice.domain.CreditCardDto;


@FeignClient(name = "credit-card-service")
@RibbonClient(name = "credit-card-service")
public interface CreditCardServiceProxy {

	@GetMapping("/filetrans/trans/{caseNumber}")
	public CreditCardDto download(@PathVariable("caseNumber") String caseNo);
}
