package com.xgang.cloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author xugang
 * @date 2020/7/9 15:32
 */
@Service
public class HelloService {
	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "error")
	public String hiService(String name) {
		return restTemplate.getForObject("http://EUREKA-CLIENT-01/hi?name=" + name, String.class);
	}

	public String error(String name) {
		return "hi, " + name + ", sorry, error!";
	}
}
