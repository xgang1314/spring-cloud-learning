package com.xgang.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ServiceConsulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceConsulApplication.class, args);
	}

}
