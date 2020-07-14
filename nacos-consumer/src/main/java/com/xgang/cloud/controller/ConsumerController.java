package com.xgang.cloud.controller;

import com.xgang.cloud.service.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xugang
 * @date 2020/7/10 10:04
 */
@RestController
public class ConsumerController {

	@Autowired
	ProviderClient providerClient;

	@GetMapping("/hi-feign")
	public String hi(@RequestParam String name) {
		return providerClient.hi(name);
	}
}
