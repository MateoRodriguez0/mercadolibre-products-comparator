package com.compare.products.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "servicio-categorias")
public interface CategoriasClient {

	@GetMapping(value = "/details/{id}")
	public ResponseEntity<?> getTypeOfPublicationByProductCategory(
							@PathVariable(name = "id") String categoryId);
}
