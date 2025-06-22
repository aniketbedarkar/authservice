package com.nest.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class AuthserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserviceApplication.class, args);
	}

}
