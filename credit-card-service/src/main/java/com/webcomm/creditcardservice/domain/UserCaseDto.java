package com.webcomm.creditcardservice.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCaseDto {

	private String caseNo;
	private String msgNo;
	private String cardNo;
	private String name;
	private Timestamp uploadTime;
}
