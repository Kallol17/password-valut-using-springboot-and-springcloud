package com.practicespringboot.passwordvault.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practicespringboot.passwordvault.exception.DataNotFoundException;
import com.practicespringboot.passwordvault.exception.InvalidRequestException;
import com.practicespringboot.passwordvault.model.UserInput;
import com.practicespringboot.passwordvault.model.UserViewForAdmin;
import com.practicespringboot.passwordvault.model.Users;
import com.practicespringboot.passwordvault.service.UserService;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	

	@PostMapping("/sign-up")
	public ResponseEntity<?> createUser(@RequestBody UserInput userInput) {

		if (null != userInput && null != userInput.getUserName() && !userInput.getUserName().isEmpty() && null != userInput.getUserPass()
				&& !userInput.getUserPass().isEmpty()) {
			
			userService.createUser(userInput);
			return new ResponseEntity<>(userInput, HttpStatus.CREATED);
		} else {
			throw new InvalidRequestException("Invalid User Input !!! ");
		}
	}
	

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserInput userInput) {
		
		if (null != userInput && null != userInput.getUserName() && !userInput.getUserName().isEmpty() && null != userInput.getUserPass()
				&& !userInput.getUserPass().isEmpty()) {
			
			String jwtToken = userService.loginUser(userInput);
			return new ResponseEntity<>(jwtToken, HttpStatus.OK);
		} else {
			throw new InvalidRequestException("Invalid User Input !!! ");
		}
	}
	
	
	@SecurityRequirement(name = "basicAuth")
	@DeleteMapping("/delete-user")
	public ResponseEntity<?> deleteUser() {
		
		String userName = SecurityContextHolder.getContext()         
				                               .getAuthentication()
				                               .getName();           //Extracting the username from the auth provided
																	 //Code gets here means user is authenticated and authorized
		Optional<Users> user = userService.deleteUserByName(userName);

		return new ResponseEntity<>(user.get(), HttpStatus.GONE);
	}

	
	//Admin API
	@SecurityRequirement(name = "basicAuth")
	@GetMapping("/user-dashboard")
	public ResponseEntity<List<UserViewForAdmin>> viewUserDefaultAll(@RequestParam @Nullable String userName){
		
		userName = (null!= userName && !userName.isBlank()) ? userName : null;
		
		List<UserViewForAdmin> optionalUserViewForAdminList = userService.viewUserDefaultAllService(userName);
		
		if(null!=optionalUserViewForAdminList) {
			
			return new ResponseEntity<>(optionalUserViewForAdminList, HttpStatus.OK);
		}
		throw new DataNotFoundException("Invalid user name - " + userName);
	}

}
