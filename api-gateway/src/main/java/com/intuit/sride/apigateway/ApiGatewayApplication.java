package com.intuit.sride.apigateway;

import com.intuit.sride.apigateway.filter.RequestIDFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Calendar;

/**
 * This is the application entry point for core services when server start.
 */

@SpringBootApplication
@Log4j2
@EnableFeignClients(basePackages = {"com.intuit"})
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
		log.info("Current TimeZone: {}", Calendar.getInstance().getTimeZone().getID());
	}


	@Bean
	public RequestIDFilter addRequestID() {
		return new RequestIDFilter();
	}
}
