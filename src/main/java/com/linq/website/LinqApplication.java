package com.linq.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LinqApplication {

	public static void main(String[] args) {
		//To disable the AWS SDK for Java 1.x deprecation announcement
		System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");
		SpringApplication.run(LinqApplication.class, args);
	}

}
