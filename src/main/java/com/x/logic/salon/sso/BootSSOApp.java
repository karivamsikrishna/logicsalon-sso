package com.x.logic.salon.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class BootSSOApp extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(BootSSOApp.class, args);
	}
}