package com.webcomm.gateway.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="gateway_log")
public class HttpMessageLog {
	
	public HttpMessageLog() {
		
	}
	
	public HttpMessageLog(long t) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr = df.format(t); 
        this.logTime = Timestamp.valueOf(timeStr);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="log_time")
	private Timestamp logTime;
	
	@Column(name="message", length=1022)
	private String message;
	
	@Column(name="msg_body")
	private String msgBody;
}
