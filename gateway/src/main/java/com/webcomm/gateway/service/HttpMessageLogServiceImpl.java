package com.webcomm.gateway.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.gateway.model.HttpMessageLog;
import com.webcomm.gateway.repository.HttpMessageLogRepository;

@Service
public class HttpMessageLogServiceImpl implements HttpMessageLogService {

	@Autowired
	HttpMessageLogRepository httpMessageLogRepository;
	
//	@Override
//	public List<HttpMessageLog> findByLogTime(Timestamp from, Timestamp to) {
//		return httpMessageLogRepository.findByLogTime(from, to);
//	}

	@Override
	public void save(HttpMessageLog msg) {
		httpMessageLogRepository.save(msg);
	}

}
