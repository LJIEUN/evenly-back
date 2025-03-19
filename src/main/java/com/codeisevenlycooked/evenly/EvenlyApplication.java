package com.codeisevenlycooked.evenly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EvenlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvenlyApplication.class, args);
	}

}
