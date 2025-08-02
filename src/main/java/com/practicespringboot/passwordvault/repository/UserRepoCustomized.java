package com.practicespringboot.passwordvault.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.practicespringboot.passwordvault.model.Users;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Component
public class UserRepoCustomized {

	@Autowired
	private EntityManager entityManager;
	
	
	public List<Users> viewUserDefaultAll(String userName){
		
		String jpql = "SELECT user FROM Users user WHERE (:userName IS NULL OR user.userName=:userName)";
		TypedQuery<Users> query = entityManager.createQuery(jpql, Users.class);
		query.setParameter("userName", userName);
		return (!query.getResultList().isEmpty()) ? query.getResultList() : null;

	}
}
