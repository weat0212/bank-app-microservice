package com.webcomm.gateway.service;

import java.sql.Timestamp;
import java.util.List;

import com.webcomm.gateway.model.HttpMessageLog;

public interface HttpMessageLogService {

//	List<HttpMessageLog> findByLogTime(Timestamp from, Timestamp to);

	void save(HttpMessageLog msg);
}
