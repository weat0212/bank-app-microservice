package com.webcomm.userauthenticationservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.webcomm.userauthenticationservice.model.User;
import com.webcomm.userauthenticationservice.web.dto.JwtDto;
import com.webcomm.userauthenticationservice.web.dto.UserDto;
import com.webcomm.userauthenticationservice.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
    
    void findByEmailAndSaveToken(String email, String jwt);
    
    User findByJwtToken(String jwt);
}
