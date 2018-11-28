package com.dicka.microservicezipkinserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import zipkin.server.EnableZipkinServer;

@SpringBootApplication
//@EnableZipkinServer
public class MicroserviceZipkinServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceZipkinServerApplication.class, args);
	}
}
