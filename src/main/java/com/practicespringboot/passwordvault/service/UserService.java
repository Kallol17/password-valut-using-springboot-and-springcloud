package com.practicespringboot.passwordvault.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.practicespringboot.passwordvault.exception.InvalidRequestException;
import com.practicespringboot.passwordvault.exception.UnauthorizedUserException;
import com.practicespringboot.passwordvault.model.UserInput;
import com.practicespringboot.passwordvault.model.UserViewForAdmin;
import com.practicespringboot.passwordvault.model.Users;
import com.practicespringboot.passwordvault.repository.UserRepoCustomized;
import com.practicespringboot.passwordvault.repository.UserRepository;
import com.practicespringboot.passwordvault.util.JwtUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRepoCustomized userRepoCustomized;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	public void createUser(UserInput userInput) {
		
		//Check if username already exists
		if(userRepository.findByUserName(userInput.getUserName()).isPresent()) {
			
			throw new InvalidRequestException("Username alresy exists, Please select another !!! ");
		}
		
		Users user = new Users();
		user.setUserName(userInput.getUserName());
		user.getRoles().add("USER");
//		user.getRoles().add("ADMIN");
		user.setUserPass(passwordEncoder.encode(userInput.getUserPass()));
		userRepository.save(user);
	}
	
	
	public List<Users> viewAllUser(){
		
		return userRepository.findAll();
	}
	
	
	public Optional<Users> viewSpecificUser(String userId) {
		
		return userRepository.findById(userId);
	}
	
	public Optional<Users> findbyUserName(String userName) {
		
		return userRepository.findByUserName(userName);
	}
	
	public void deleteUser(String userId) {
		
		userRepository.deleteById(userId);
	} 
	
	public Optional<Users> deleteUserByName(String username) {
		
		Optional<Users> user = userRepository.findByUserName(username);
		if(user.isPresent()) {
			
			userRepository.delete(user.get());
		}
		return user;
	} 
	
	
	public List<UserViewForAdmin> viewUserDefaultAllService(String userName){
		
		List<Users> userList = userRepoCustomized.viewUserDefaultAll(userName);
		List<UserViewForAdmin> userViewForAdminList = new LinkedList<>();
		
		if(null != userList) {
			
			userViewForAdminList = userList.stream().map(user -> {

														UserViewForAdmin userViewForAdmin = new UserViewForAdmin();
														userViewForAdmin.setUsername(user.getUserName());
														userViewForAdmin.setArticleCount(user.getArticleList().size());
														return userViewForAdmin;
													}).toList();
		}
		return userViewForAdminList;
	}


	public String loginUser(UserInput userInput) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					userInput.getUserName(), userInput.getUserPass()));
			
			UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(userInput.getUserName());
			
			return jwtUtil.generateToken(userDetails);		
		} catch (Exception e) {
			
			throw new UnauthorizedUserException("Username or Password not found :(");
		}	
	}
}
