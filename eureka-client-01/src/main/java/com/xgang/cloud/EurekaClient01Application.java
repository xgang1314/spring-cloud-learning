package com.xgang.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
@RefreshScope
@EnableDiscoveryClient
public class EurekaClient01Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClient01Application.class, args);
	}

	@Value("${server.port}")
	String port;

	@Value("${spring.application.name}")
	String serverName;

//	@Value("${foo}")
//	String foo;

	@GetMapping("/hi")
	public String home(@RequestParam(value = "name", defaultValue = "xgang") String name) {
		return "hi " + name + ", i am from " + serverName + " port : " + port + "----------";
	}
}
