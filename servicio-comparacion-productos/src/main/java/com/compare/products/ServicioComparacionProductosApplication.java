package com.compare.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ServicioComparacionProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioComparacionProductosApplication.class, args);
	}

}
