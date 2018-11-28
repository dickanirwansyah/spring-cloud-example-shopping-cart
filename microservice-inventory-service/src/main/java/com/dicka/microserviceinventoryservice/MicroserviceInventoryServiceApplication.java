package com.dicka.microserviceinventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class MicroserviceInventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceInventoryServiceApplication.class, args);
	}
}
