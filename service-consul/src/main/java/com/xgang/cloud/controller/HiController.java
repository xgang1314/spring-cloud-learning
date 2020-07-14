package com.xgang.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xugang
 * @date 2020/7/9 21:10
 */
@RefreshScope
@RestController
public class HiController {

	@Value("${server.port}")
	String port;

	@Value("${spring.application.name}")
	String name;

	@Value("${foo}")
	String foo;

	@GetMapping("hi")
	public String hi() {
		return "hi, " + name + ", port: " + port + "---------" + foo;
	}
}
