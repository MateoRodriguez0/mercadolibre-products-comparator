package com.compare.products.categoria.analisis.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.compare.products.categoria.analisis.services.CategoryService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class AnalysisCategoryController {

	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getTypeOfPublicationByProductCategory(@PathVariable(name = "id") String categoryId) {
		System.out.println(categoryServices.findCategorybyId(categoryId));
		
		return ResponseEntity.ok(categoryServices.getDetailsByCategory(categoryId));
	}
	
	
	@GetMapping(value = "/d")
	public void gcategorias() {
		
		ResponseEntity<JsonNode> nodee =clientHttp.
				getForEntity(URI.create("https://api.mercadolibre.com/sites"), JsonNode.class);
		System.out.println(nodee.getBody());
	}
	
	@Autowired
	RestTemplate clientHttp;
	
	@Autowired
	CategoryService categoryServices;
	
	
}
