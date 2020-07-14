package com.xgang.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xugang
 * @date 2020/7/10 10:03
 */
@FeignClient("nacos-provider")
public interface ProviderClient {

	@GetMapping("/hi")
	String hi(@RequestParam(value = "name", defaultValue = "xgang", required = false) String name);
}
