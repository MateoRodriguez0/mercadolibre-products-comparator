package com.compare.products.categorias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compare.products.categorias.services.CategoryService;
import com.compare.products.categorias.services.implementations.DomainsCompatiblesService;

@RestController
@RequestMapping(value = "/compatibles")
public class CompatiblitiesController {


	@GetMapping(value = "/domains")
	public ResponseEntity<?> compatibeDomains(@RequestParam(name = "ids") String[] categories) {
		try {
			return new ResponseEntity<Boolean>(domainsService.areCompatibles(categories),
					domainsService.getStatus());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping(value = "/categories")
	public ResponseEntity<?> compatibeCategories(@RequestParam(name = "ids") String[] categories) {
		try {
			return ResponseEntity.ok(categoryService.areCompatibles(categories));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}
	
	@Autowired
	private DomainsCompatiblesService domainsService;
	
	@Autowired
	@Qualifier("categoriesCompatibles")
	private CategoryService categoryService;
}
