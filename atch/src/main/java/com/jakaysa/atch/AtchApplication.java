package com.jakaysa.atch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AtchApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtchApplication.class, args);
	}

}
