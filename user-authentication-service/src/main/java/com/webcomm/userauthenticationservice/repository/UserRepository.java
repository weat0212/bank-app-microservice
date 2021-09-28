package com.webcomm.userauthenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.webcomm.userauthenticationservice.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	
	@Query(value = "SELECT * FROM user WHERE jwt_token = ?1", nativeQuery = true)
	User findByJwtToken(String jwt);
}
