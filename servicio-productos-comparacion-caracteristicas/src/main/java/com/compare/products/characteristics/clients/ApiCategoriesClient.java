package com.compare.products.characteristics.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "apiCategoriesClient", url = "${compare.products.paths.api}")
public interface ApiCategoriesClient {
	
	@GetMapping(value = "${compare.products.paths.technical_espec.output}")
	public ResponseEntity<JsonNode> technicalSpecs(@PathVariable(name = "id")String id);

}