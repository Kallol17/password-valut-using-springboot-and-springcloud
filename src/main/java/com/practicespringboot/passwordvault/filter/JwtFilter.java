package com.practicespringboot.passwordvault.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.practicespringboot.passwordvault.service.UserDetailServiceImpl;
import com.practicespringboot.passwordvault.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader("Authorization");
		String userName = null;
		String jwtToken = null;
		
		if(null!=authorizationHeader && authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
			userName = jwtUtil.extractUserName(jwtToken);
		}
		
		if(null!=userName) {
			UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(userName);
			if(!jwtUtil.isTokenExpired(jwtToken)) {
				// Extract roles from claims
				List<String> roles = jwtUtil.extractRoles(jwtToken);
				
				List<SimpleGrantedAuthority> authorities = roles.stream()
						                                        .map(SimpleGrantedAuthority::new)
						                                        .toList();
				
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
				//principal: the authenticated user (usually a UserDetails object)
				//credentials: password/token (you can set null here if already validated)
				//authorities: roles/permissions from your UserDetails
				
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	
}
