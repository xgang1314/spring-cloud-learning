package com.xgang.cloud.rateLimiter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author xugang
 * @date 2020/7/9 17:09
 */
public class HostAddrKeyResolver implements KeyResolver {
	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
		return Mono.just(exchange.getRequest().getURI().getPath());
	}
}
