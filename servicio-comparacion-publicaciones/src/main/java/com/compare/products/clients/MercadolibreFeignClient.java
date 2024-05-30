package com.compare.products.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "meliApi",url = "https://api.mercadolibre.com")
public interface MercadolibreFeignClient {
	
	@GetMapping(value = "/items/{id}")
	public ResponseEntity<JsonNode> getItem(@PathVariable(name = "id") String id,
			@RequestHeader(name = "Authorization")String token);
	
	@GetMapping(value = "/products/{id}")
	public ResponseEntity<JsonNode> getProduct(@PathVariable(name = "id") String id,
			@RequestHeader(name = "Authorization")String token);

}
