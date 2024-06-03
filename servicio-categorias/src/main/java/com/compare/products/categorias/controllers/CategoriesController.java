package com.compare.products.categorias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compare.products.categorias.models.DetailsCategory;
import com.compare.products.categorias.services.CategoryService;

import feign.FeignException;


@RestController
@RequestMapping(value = "/categories")
public class CategoriesController {

	@GetMapping(value = "/details/{id}")
	public ResponseEntity<DetailsCategory> getTypeOfPublicationByProductCategory(
			@PathVariable(name = "id") String categoryId) {
		try {
			DetailsCategory dCategory=categoryServices.getDetailsByCategory(categoryId);
			return ResponseEntity.ok(dCategory);
		} catch (FeignException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	
	@Autowired
	private CategoryService categoryServices;
	
}
