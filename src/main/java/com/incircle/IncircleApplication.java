package com.incircle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IncircleApplication {

	public static void main(String[] args) {
		SpringApplication.run(IncircleApplication.class, args);
	}

}
