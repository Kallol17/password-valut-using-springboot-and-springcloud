package com.practicespringboot.passwordvault.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practicespringboot.passwordvault.model.Article;
import com.practicespringboot.passwordvault.model.Users;

public interface ArticleRepository extends JpaRepository<Article, String> {

	List<Article> findByUsers(Users user);

}
