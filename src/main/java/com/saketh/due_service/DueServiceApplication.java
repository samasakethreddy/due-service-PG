package com.saketh.due_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class DueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DueServiceApplication.class, args);
	}

}
