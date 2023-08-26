package com.intuit.sride.onboardingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories
public class OnboardingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnboardingServiceApplication.class, args);
	}

}
