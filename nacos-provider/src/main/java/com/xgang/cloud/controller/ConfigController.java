package com.xgang.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xugang
 * @date 2020/7/10 10:19
 */
@RestController
@RefreshScope
public class ConfigController {

//	@Value("${foo}")
//	String foo;

	@GetMapping("/foo")
	public String get() {
		return "foo";
	}
}
