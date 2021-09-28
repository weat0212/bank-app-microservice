package com.webcomm.userauthenticationservice.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webcomm.userauthenticationservice.model.Role;
import com.webcomm.userauthenticationservice.model.User;
import com.webcomm.userauthenticationservice.repository.RoleRepository;
import com.webcomm.userauthenticationservice.repository.UserRepository;
import com.webcomm.userauthenticationservice.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(UserRegistrationDto registration){
 
        User user = new User();
        Role role = roleRepository.findByName("ROLE_USER");
        
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(role));
        
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
	public void findByEmailAndSaveToken(String email, String jwt) {
		User userFound = userRepository.findByEmail(email);
		if (userFound == null){
            throw new UsernameNotFoundException("Username: " + email + "Not Found");
        } else {
        	userFound.setJwtToken(jwt);
    		userRepository.save(userFound);
        }
	}

	@Override
	public User findByJwtToken(String jwt) {
		return userRepository.findByJwtToken(jwt);
	}
}
