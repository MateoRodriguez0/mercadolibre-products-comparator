package com.compare.products.characteristics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioProductosComparacionEspecificacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioProductosComparacionEspecificacionesApplication.class, args);
	}

}
