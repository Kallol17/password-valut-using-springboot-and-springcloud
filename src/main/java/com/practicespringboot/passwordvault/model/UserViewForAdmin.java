package com.practicespringboot.passwordvault.model;

public class UserViewForAdmin {
	
	private String username;
	private int articleCount;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getArticleCount() {
		return articleCount;
	}
	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

}
