package com.webcomm.userauthenticationservice.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.google.common.net.HttpHeaders;
import com.webcomm.userauthenticationservice.filter.AuthoritiesLoggingAfterFilter;
import com.webcomm.userauthenticationservice.filter.AuthoritiesLoggingAtFilter;
import com.webcomm.userauthenticationservice.filter.RequestValidationBeforeFilter;
import com.webcomm.userauthenticationservice.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	  super.configure(web);
	  // @formatter:off
	  web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

			.cors().configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:8765", "http://localhost:4200"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setExposedHeaders(Arrays.asList("Authorization"));
						config.setMaxAge(3600L);
						return config;
					}
				}).and().csrf().disable();

		// Here the jwt token is added
		http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
			.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
			.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class).authorizeRequests();

		http.formLogin().loginProcessingUrl("/login/process").successHandler(successHandler()).failureHandler(failureHandler());
		http.authorizeRequests().mvcMatchers("/registration**", "/js/**", "/css/**", "/img/**", "/webjars/**","/login**").permitAll()
			.and().authorizeRequests().mvcMatchers("/filetrans/**", "/").authenticated();

		http.logout().deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
				.permitAll();
	}
	
	
	private AuthenticationSuccessHandler successHandler() {
	    return new AuthenticationSuccessHandler() {
	        @Override
	        public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
	                HttpServletResponse httpServletResponse, Authentication authentication)
	                throws IOException, ServletException {
	        	
	        	String jwt = httpServletRequest.getHeader(SecurityConstants.JWT_HEADER);

	    		// If we got the token, validation the token.
	    		if (jwt != null) {
	    			try {
	    				SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

	    				// There do the validation of the token
	    				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

	    				String username = String.valueOf(claims.get("username"));
	    				String authorities = (String) claims.get("authorities");
	    				
	    				Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
	    						AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
	    				SecurityContextHolder.getContext().setAuthentication(auth);
	    				
	    			} catch (Exception e) {
	    				throw new BadCredentialsException("Invalid Token Received");
	    			}
	    		}
	        	
	        	if(authentication != null) {
	    			SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
	    			String newJwt = Jwts.builder().setIssuer("Andy Wang").setSubject("Jwt Token")
	    					.claim("username", authentication.getName())	//Claim define the opened information to share
	    					.claim("authorities", populateAuthorities(authentication.getAuthorities()))
	    					.setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + 3000000))	// 30sec expiration time 
	    					.signWith(key).compact();
	    			httpServletResponse.setHeader(SecurityConstants.JWT_HEADER, newJwt);

	    			userService.findByEmailAndSaveToken(authentication.getName(), newJwt);
	    			
	    		}
	        	
//	            httpServletResponse.getWriter().append("Login Success!");
	            httpServletResponse.setStatus(200);
	        }
	    };
	}
	
	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
        	authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
	}
	

	private AuthenticationFailureHandler failureHandler() {
	    return new AuthenticationFailureHandler() {
	        @Override
	        public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
	                HttpServletResponse httpServletResponse, AuthenticationException e)
	                throws IOException, ServletException {
	            httpServletResponse.getWriter().append("Authentication failure");
	            httpServletResponse.setStatus(401);
	        }
	    };
	}

	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTION"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder);
		return auth;
	}

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
	    StrictHttpFirewall firewall = new StrictHttpFirewall();
	    firewall.setAllowUrlEncodedSlash(true);
	    firewall.setAllowSemicolon(true);
	    return firewall;
	}
}
