package com.compare.products.characteristics.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "servicioCategorias", url = "http://localhost:9090")
public interface CategoriesClient {
	
	@GetMapping(value = "/compatibles/domains")
	public ResponseEntity<Boolean> compatibleToCompare(
			@RequestParam(name = "ids") String[] categories);

}
