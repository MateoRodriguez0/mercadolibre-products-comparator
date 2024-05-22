package com.compare.products.info.commercial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioProductosComparacionInfoComercialApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioProductosComparacionInfoComercialApplication.class, args);
	}

}
