package com.practicespringboot.passwordvault.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practicespringboot.passwordvault.exception.InvalidRequestException;
import com.practicespringboot.passwordvault.model.Article;
import com.practicespringboot.passwordvault.model.ArticleCreationInput;
import com.practicespringboot.passwordvault.service.ArticleService;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth") // Applies to all methods in this class
@RestController
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	
	@GetMapping("/view-my-articles")
	public ResponseEntity<?> viewAllArticle() {
		
		return new ResponseEntity<>(articleService.viewArticleofUser(), HttpStatus.OK);
	}
	
	
	@PostMapping("/create-article")
	public ResponseEntity<?> createArticle(@Valid @RequestBody ArticleCreationInput articleCreationInput) {

		if (null != articleCreationInput) {
			
			Article articleInDb = articleService.createArticle(articleCreationInput);
			return new ResponseEntity<>(articleInDb, HttpStatus.OK);
		} else {
			
			throw new InvalidRequestException("Invalid User Input !!! ");
		}
	}
	
	
	@DeleteMapping("/delete-article")
	public ResponseEntity<?> deleteArticle(@RequestParam @Nullable String articleId){
		
		return new ResponseEntity<>("Deleted article : ".concat(articleService.deleteArticle(articleId)), HttpStatus.GONE);
	}
		
}
