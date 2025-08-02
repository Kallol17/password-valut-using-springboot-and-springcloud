package com.practicespringboot.passwordvault.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Users {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or GenerationType.AUTO
	private Long userID;
	
	@Column(unique = true, nullable = false)
	private String userName;
	private String userPass;
	private Set<String> roles=new HashSet<String>();
	
	@OneToMany(mappedBy = "users" , cascade = CascadeType.ALL , orphanRemoval = true)
	@JsonManagedReference
	private List<Article> articleList = new LinkedList<>();

	public Long getUserID() {
		return userID;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
		
}
