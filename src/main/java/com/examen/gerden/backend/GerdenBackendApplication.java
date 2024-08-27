package com.examen.gerden.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GerdenBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerdenBackendApplication.class, args);
	}

}
