package com.compare.products.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "servicio-productos-comparacion-caracteristicas")
public interface CaracteristicasClient {

	
}
