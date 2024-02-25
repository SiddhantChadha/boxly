package com.siddhant.boxly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BoxlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoxlyApplication.class, args);
	}

}
