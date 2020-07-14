package com.xgang.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xugang
 * @date 2020/7/9 16:01
 */
@FeignClient(value = "EUREKA-CLIENT-01", fallback = EurekaClientHystrix.class)
public interface EurekaClient {

	@GetMapping(value = "/hi")
	String sayHiFromClient(@RequestParam(value = "name") String name);
}
