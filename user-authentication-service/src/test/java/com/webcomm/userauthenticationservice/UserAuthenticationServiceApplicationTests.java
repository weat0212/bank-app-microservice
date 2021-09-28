package com.webcomm.userauthenticationservice;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@SpringBootTest
class UserAuthenticationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testPasswordEncoders() {
		
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		
		System.out.println(new DelegatingPasswordEncoder("bcrypt", encoders)
				.encode("password"));
	}
}
