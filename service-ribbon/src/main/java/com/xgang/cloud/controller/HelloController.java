package com.xgang.cloud.controller;

import com.xgang.cloud.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xugang
 * @date 2020/7/9 15:33
 */
@RestController
public class HelloController {

	@Autowired
	HelloService helloService;

	@GetMapping("/hi")
	public String hi(@RequestParam String name) {
		return helloService.hiService(name);
	}
}
