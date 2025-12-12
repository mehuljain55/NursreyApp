package com.ayush.nursery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ayush.nursery")
public class NurseryApplication {

	public static void main(String[] args) {
		SpringApplication.run(NurseryApplication.class, args);
	}

}
