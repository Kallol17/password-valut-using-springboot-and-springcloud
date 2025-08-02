package com.practicespringboot.passwordvault.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.practicespringboot.passwordvault.model.Users;
import com.practicespringboot.passwordvault.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Users user = userRepository.findByUserName(username)
					  .orElseThrow(()->new UsernameNotFoundException("Invalid UserName : " + username + " !!!"));
		
		return User.withUsername(user.getUserName())
					.password(user.getUserPass())
					.roles(user.getRoles().toArray(new String[0]))
					.build();
	}

}
