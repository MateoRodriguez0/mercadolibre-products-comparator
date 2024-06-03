package com.compare.products.categorias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableCaching
public class ServicioAnalisisCategoriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioAnalisisCategoriaApplication.class, args);
	}

}
