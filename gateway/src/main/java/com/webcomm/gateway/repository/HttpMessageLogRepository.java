package com.webcomm.gateway.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.webcomm.gateway.model.HttpMessageLog;

public interface HttpMessageLogRepository extends JpaRepository<HttpMessageLog, Long> {

//	@Query("SELECT g FROM gateway_log g WHERE g.log_time BETWEEN ?1 AND ?2")
//	public List<HttpMessageLog> findByLogTime(Timestamp from, Timestamp to);
}
