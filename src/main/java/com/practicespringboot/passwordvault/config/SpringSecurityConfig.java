package com.practicespringboot.passwordvault.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.practicespringboot.passwordvault.filter.JwtFilter;
import com.practicespringboot.passwordvault.service.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;
	

	@Bean
	@Order(1)
	public SecurityFilterChain articleApiSecurity(HttpSecurity http, PasswordEncoder passwordEncoder,UserDetailServiceImpl userDetailServiceImpl) throws Exception{
		
		http.securityMatcher("/article/**") //This filter chain is only used for /article/**
		        .authorizeHttpRequests(auth->auth
				.requestMatchers("/article/**")
				.hasAnyAuthority("ROLE_USER")
			)
			.csrf(csrf->csrf.disable()) // disable CSRF for non-browser clients
			.sessionManagement(session -> session //To disable the session creation -> APIs will be Stateless
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); 
			
		
		return http.build();
	}
	
	
	@Bean
	@Order(2)
	public SecurityFilterChain userApiSecurity(HttpSecurity http, PasswordEncoder passwordEncoder,UserDetailServiceImpl userDetailServiceImpl) throws Exception{		
		
		http.securityMatcher("/user/**") //This filter chain is only used for /user/**
		        .authorizeHttpRequests(auth->auth
		        .requestMatchers("/user/sign-up", "/user/login").permitAll()   //Public API(login is authenticated internally)
				.requestMatchers("/user/delete-user").hasAnyAuthority("ROLE_USER")
		        .requestMatchers("/user/user-dashboard").hasAnyAuthority("ROLE_ADMIN")
		        .anyRequest()
		        .denyAll()
		        )	
			.httpBasic(withDefaults())
			.csrf(csrf->csrf.disable()) // disable CSRF for non-browser clients
			.sessionManagement(session -> session //To disable the session creation -> APIs will be Stateless
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); 		
		
		return http.build();
	}
	
	
	@Bean
	@Order(3)
	public SecurityFilterChain swaggerApiSecurity(HttpSecurity http, PasswordEncoder passwordEncoder,UserDetailServiceImpl userDetailServiceImpl) throws Exception{		
		
		http.securityMatcher("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html") //This filter chain is only used for swagger endpoints
		        .authorizeHttpRequests(auth->auth
		        .anyRequest()
		        .permitAll()
		        );		
		
		return http.build();
	}
	
	@Bean
	@Order(100)
	public SecurityFilterChain defaultApiNotAccessable(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(auth->auth
				.anyRequest().denyAll() // No access to the API endpoints not matched already
			);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();	
	}
	
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	
}
