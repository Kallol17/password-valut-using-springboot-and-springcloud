package com.practicespringboot.passwordvault.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private final int jwtTokenValidayionDuration;  // token will be valid for this minutes
	private final String SECRET_KEY; // needs to be grater than 32 bytes
	

	public JwtUtil(@Value("${jwtSecretKey}") String SECRET_KEY, @Value("${jwtTokenValidationLimit}") int jwtTokenValidayionDuration) {
		this.SECRET_KEY = SECRET_KEY;
		this.jwtTokenValidayionDuration = jwtTokenValidayionDuration;
	}

	
	//Method to generate JWT token
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		List<String> roleList = userDetails.getAuthorities()
										.stream()
										.map(GrantedAuthority::getAuthority) // add "ROLE_" prefix
										.toList();
		claims.put("roles", roleList);
		return createToken(userDetails.getUsername(), claims);
	}

	private String createToken(String subject, Map<String, Object> claims) {
		return Jwts.builder()
				   .claims(claims)
				   .subject(subject)  //Identifier of token -> userName is this case
				   .header().empty().add("type", "JWT")  //Optional
				   .and()
				   .issuedAt(new Date(System.currentTimeMillis()))
				   .expiration(new Date(System.currentTimeMillis() + (1000*60*jwtTokenValidayionDuration)))
				   .signWith(getSigningKey())
				   .compact();
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	} 
	
	
	
	public String extractUserName(String jwtToken) {
		return extractAllClaims(jwtToken).getSubject();
	}
	
	
	private Claims extractAllClaims(String jwtToken) {
		return Jwts.parser()
				   .verifyWith(getSigningKey())
				   .build()
				   .parseSignedClaims(jwtToken)
				   .getPayload();
	}
	
	
	public Boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}
	
	
	public Date extractExpiration(String jwtToken) {
		return extractAllClaims(jwtToken).getExpiration();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> extractRoles(String jwtToken){
		return extractAllClaims(jwtToken).get("roles", List.class);
	}
}



























