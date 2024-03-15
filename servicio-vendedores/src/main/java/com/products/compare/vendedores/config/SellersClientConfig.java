package com.products.compare.vendedores.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.products.compare.vendedores.services.AuthorizathionService;

import feign.RequestInterceptor;

@Configuration
public class SellersClientConfig {

	@Bean
	RequestInterceptor interceptor(AuthorizathionService authorizathionService) {
		return template -> template
				.header("Authorization", authorizathionService.getToken());
	}
	
	

	
}
