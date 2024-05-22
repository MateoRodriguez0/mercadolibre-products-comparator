package com.compare.products.opinions.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcurrentConfig {

	@Bean(name = "virtualThread")
	ExecutorService executor() {
		return Executors.newVirtualThreadPerTaskExecutor();
	}
	

}
