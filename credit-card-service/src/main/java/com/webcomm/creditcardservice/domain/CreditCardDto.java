package com.webcomm.creditcardservice.domain;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@XmlRootElement(name = "CREDIT-CARD")
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDto {

	@XmlElement(name = "EMI-NO")
	private String no;
	
	@XmlElement(name = "NUMBER")
	private String cno;
	
	@XmlElement(name = "NAME")
	private String name;
	
	
	private String userId;
}
