package com.compare.products.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "servicio-productos-analisis-opiniones")
public interface AnalisisOpinionesClient {

}
