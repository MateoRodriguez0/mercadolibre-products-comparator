package com.mercadolibre.productscomparator.vendedores.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mercadolibre.productscomparator.vendedores.services.AuthorizathionService;

import feign.RequestInterceptor;

@Configuration
public class SellersClientConfig {

	@Bean
	RequestInterceptor interceptor(AuthorizathionService authorizathionService) {
		return template -> template
				.header("Authorization", authorizathionService.getToken());
	}
	
	

	
}
