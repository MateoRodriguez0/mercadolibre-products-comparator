package com.compare.products.characteristics.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBeans {

	
	@Bean
	@Scope("prototype")
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
