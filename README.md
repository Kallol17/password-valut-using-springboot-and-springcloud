🔐 Password Vault - Secure Credential Manager

A simple password vault application built using Spring Boot, featuring JWT-based authentication, role-based access control, and a secure interface to store and manage passwords. This project demonstrates secure backend practices for modern web applications.4


📌 Features

🔒 User Sign-Up & Login with JWT Authentication

👤 Role-based access control (ADMIN / USER)

🗝️ Store, Retrieve & Delete passwords securely

🧾 7 REST APIs exposed for user and vault operations

🔐 Password encryption using BCrypt before storage

🧪 Swagger/OpenAPI integrated for API documentation

🛡️ Basic Authentication enabled for selected endpoints


🛠️ Tech Stack

Java 17
Spring Boot 3.1.5
Spring Security (JWT + HTTP Basic)
Hibernate & JPA
MySQL
Swagger (Springdoc OpenAPI)
Maven


📂 API Endpoints (Examples)
Method          	Endpoint                        	Description
POST	          /user/sign-up	                     Register a new user
POST	          /user/login	                     User login (returns JWT)
DELETE            /user/delete-user                  Delete user and all stored passwards of user (Basic authentication required)
GET	              /article/view-my-articles      	 Fetch all stored passwords of user (JWT required)
POST	          /article/create-article            Save a new password (JWT required)
DELETE      	  /article/delete-article/{id}       Delete password by ID (JWT required)
GET               /user/user-dashboard	             List all users (Admin access only)