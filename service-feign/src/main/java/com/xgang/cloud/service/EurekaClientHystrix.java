package com.xgang.cloud.service;

import org.springframework.stereotype.Component;

/**
 * @author xugang
 * @date 2020/7/9 16:19
 */
@Component
public class EurekaClientHystrix implements EurekaClient {
	@Override
	public String sayHiFromClient(String name) {
		return "sorry" + name;
	}
}
