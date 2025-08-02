package com.practicespringboot.passwordvault.model;

import jakarta.validation.constraints.NotNull;

public class ArticleCreationInput {
	
	private String referenceName;
	
	@NotNull(message = "Please provide valid password !!!")
	private String password;

	
	
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
	
}
