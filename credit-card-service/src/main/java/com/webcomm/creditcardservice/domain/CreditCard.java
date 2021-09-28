package com.webcomm.creditcardservice.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "credit_card_case")
public class CreditCard {

	@Id
	@NotNull
	@Size(max = 32)
	@Column(name = "case_no")
	@Pattern(regexp="^[\\d]{1,32}")
	private String caseNumber;
	
	@NotNull
	@Size(max = 32)
	@Column(name = "msg_no")
	@Pattern(regexp="^[\\w]{1,32}", message="Number and Character Accepted, Max Length:32")
	private String messageNumber;
	
	@NotNull
	@Size(max = 16, min = 16)
	@Column(name = "credit_card_no")
	@Pattern(regexp="^[\\d]{16}", message="Number Only, Max Length:16")
	private String creditNumber;
	
	@NotNull
	@Size(max = 8)
	@Column(name = "name")
	private String customerName;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "upload_time")
	private Timestamp uploadTime;
}
