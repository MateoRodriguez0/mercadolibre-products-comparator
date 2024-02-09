package com.mercadolibre.productscomparator.url.analisis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioAnalisisUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioAnalisisUrlApplication.class, args);
	
	}

}
