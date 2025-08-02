package com.practicespringboot.passwordvault.config;

import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

public class SwaggerDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
    	
        return new OpenAPI()
                .info(new Info()
                        .title("Password Vault Application APIs")
                        .version("1.0")
                        .description("API documentation of Spring Boot JWT-protected application"));
    }
}
