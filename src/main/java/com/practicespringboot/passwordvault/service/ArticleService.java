package com.practicespringboot.passwordvault.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.practicespringboot.passwordvault.exception.DataNotFoundException;
import com.practicespringboot.passwordvault.exception.InternalServerException;
import com.practicespringboot.passwordvault.model.Article;
import com.practicespringboot.passwordvault.model.ArticleCreationInput;
import com.practicespringboot.passwordvault.model.Users;
import com.practicespringboot.passwordvault.repository.ArticleRepository;
import com.practicespringboot.passwordvault.util.AESUtil;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AESUtil aesUtil;
	

	public Article createArticle(ArticleCreationInput articleCreationInput) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();                                 // Extract userName from SecurityContextHolder
		Optional<Users> user = userService.findbyUserName(userName);
		
		Article article = new Article();

		try {
			
			article.setPassword(aesUtil.encrypt(articleCreationInput.getPassword()));
		} catch (Exception e) {
			throw new InternalServerException("Error saving user data !!! Please try later :(");
		}
		
		article.setReferenceName(articleCreationInput.getReferenceName());
		article.setUsers(user.get());
		articleRepository.save(article);
		return article;
	}
	
	
	public String deleteArticle(String articleId) {
		
		if (null != articleId && !articleId.trim().isEmpty()) {
			
			Optional<Article> specificArticle = articleRepository.findById(articleId.toString());
			if (specificArticle.isPresent()) {
				
				//Check if article belongs to the user
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				String userName = authentication.getName();
				Optional<Users> user = userService.findbyUserName(userName);
				if(user.get().getUserID().equals(specificArticle.get().getUsers().getUserID())){
					
					//Delete article
					articleRepository.deleteById(articleId);
					return specificArticle.get().getReferenceName();
				}else {
					throw new DataNotFoundException("Article id - " + articleId + " does not belong to User !!!");
				}
			} else {
				throw new DataNotFoundException("Invalid article id - " + articleId);
			}
		}else {
			throw new DataNotFoundException("Invalid article id - " + articleId);
		}
	
	} 
	
	
	public List<Article> viewArticleofUser(){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();                                 // Extract userName from SecurityContextHolder
		Optional<Users> user = userService.findbyUserName(userName);	
		List<Article> articleList = articleRepository.findByUsers(user.get());      // No need to null check, User already authenticated
		
		articleList.stream()
				   .forEach(article ->{
						try {
							article.setPassword(aesUtil.decrypt(article.getPassword()));
						} catch (Exception e) {
							throw new InternalServerException("Error retreving user data !!! Please try later :(");
						}
					});
		
		return articleList;
	}
}









