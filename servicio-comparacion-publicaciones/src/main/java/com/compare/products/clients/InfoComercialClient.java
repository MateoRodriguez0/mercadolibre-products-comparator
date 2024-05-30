package com.compare.products.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "servicio-productos-comparacion-info-comercial")
public interface InfoComercialClient {

}
