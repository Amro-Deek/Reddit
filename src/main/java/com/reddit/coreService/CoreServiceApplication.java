package com.reddit.coreService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class CoreServiceApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CoreServiceApplication.class);
		app.setDefaultProperties(Map.of("server.port", "8081"));
		app.run(args);
		System.out.println("Hey Core!");
	}

}
