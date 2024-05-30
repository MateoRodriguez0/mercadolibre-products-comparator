package com.compare.products.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.compare.products.models.DetailsCategory;

@FeignClient(name = "servicio-categorias")
public interface CategoriasClient {

	@GetMapping(value = "/categories/details/{id}")
	public ResponseEntity<DetailsCategory> getTypeOfPublicationByProductCategory(
			@PathVariable(name = "id") String categoryId);
}
