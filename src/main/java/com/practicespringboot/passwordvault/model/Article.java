package com.practicespringboot.passwordvault.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Article {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or GenerationType.AUTO
	private Long articleId;
	
	private String referenceName;
	private String password;
	
	@ManyToOne
	@JoinColumn(name="userid")
	@JsonBackReference
	private Users users;
	
	
	public Long getArticleId() {
		return articleId;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
}
