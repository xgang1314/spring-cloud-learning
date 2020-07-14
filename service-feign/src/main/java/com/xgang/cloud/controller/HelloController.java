package com.xgang.cloud.controller;

import com.xgang.cloud.service.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xugang
 * @date 2020/7/9 16:02
 */
@RestController
public class HelloController {

	@Autowired
	EurekaClient eurekaClient;

	@GetMapping("/hi")
	public String hi(@RequestParam String name) {
		return eurekaClient.sayHiFromClient(name);
	}
}
