package com.compare.products.categorias.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "categoriesClient", url = "${compare.products.paths.api}")
public interface CategoriesClient {

	@GetMapping(value = "${compare.products.paths.info-category}")
	public ResponseEntity<JsonNode> infoCategories(@PathVariable(name = "id") String id);
}
