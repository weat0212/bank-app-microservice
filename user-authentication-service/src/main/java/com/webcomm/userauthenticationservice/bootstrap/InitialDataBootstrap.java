package com.webcomm.userauthenticationservice.bootstrap;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.webcomm.userauthenticationservice.model.Role;
import com.webcomm.userauthenticationservice.model.User;
import com.webcomm.userauthenticationservice.repository.RoleRepository;
import com.webcomm.userauthenticationservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("default")
public class InitialDataBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (roleRepository.count() == 0L) {
			log.debug("Loading Roles");
			loadRoles();
		}

		if (userRepository.count() == 0L) {
			log.debug("Loading Admin");
			loadAdmin();
		}

	}

	private void loadRoles() {

		Role roleAdmin = new Role("ROLE_ADMIN");
		Role roleUser = new Role("ROLE_USER");
		
		roleRepository.save(roleAdmin);
		roleRepository.save(roleUser);
	}

	private void loadAdmin() {
		
		Role role1 = roleRepository.findByName("ROLE_ADMIN");
		Role role2 = roleRepository.findByName("ROLE_USER");

		User admin = new User(1L, "Andy", "Wang", "andy.wang@webcomm.com.tw",
								passwordEncoder.encode("password"),"",
								Arrays.asList(role1, role2));
		
		userRepository.save(admin);
	}
}
