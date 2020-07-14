package com.xgang.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xugang
 * @date 2020/7/10 10:00
 */
@RestController
public class ProviderController {

	@GetMapping("/hi")
	@SentinelResource(value = "hi")
	public String hi(@RequestParam(value = "name", defaultValue = "xgang", required = false) String name) {
		return "hi, " + name;
	}
}
