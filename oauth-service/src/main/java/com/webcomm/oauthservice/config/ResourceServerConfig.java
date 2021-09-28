package com.webcomm.oauthservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig implements ResourceServerConfigurer {

	private static final String RESOURCE_ID = "credit-card-service";
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

		resources.resourceId(RESOURCE_ID);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.mvcMatchers(HttpMethod.GET, "/filetrans/download/{case_number:^[0-9]{1,32}}")
			.hasAnyRole("USER","ADMIN")
			.mvcMatchers(HttpMethod.POST, "/filetrans/trans/{case_number:^[0-9]{1,32}}")
			.hasRole("ADMIN")
			.anyRequest().denyAll()
			.and().csrf().disable()
			.logout().logoutSuccessUrl("/login?logout");
	}

}
