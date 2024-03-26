package com.compare.products.categoria.analisis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.compare.products.categoria.analisis.models.DetailsCategory;
import com.compare.products.categoria.analisis.services.CategoryService;


@RestController
public class AnalysisCategoryController {

	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getTypeOfPublicationByProductCategory(@PathVariable(name = "id") String categoryId) {
		try {
			DetailsCategory dCategory=categoryServices.getDetailsByCategory(categoryId);
			return ResponseEntity.ok(dCategory);
		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

		
	}
	
	@Autowired
	RestTemplate clientHttp;
	
	@Autowired
	CategoryService categoryServices;
	
	
}
