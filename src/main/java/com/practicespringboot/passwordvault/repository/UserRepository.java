package com.practicespringboot.passwordvault.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practicespringboot.passwordvault.model.Users;

public interface UserRepository extends JpaRepository<Users, String> {

	Optional<Users> findByUserName(String userName);
	
}
