package com.compare.products.controllers;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/compare")
@EnableFeignClients
public class CompareProductsController {

	
	@GetMapping(value = "/products", headers = "Authorizathion")
	public ResponseEntity<?> getComparaisonByUrlsOfPublications(
			@RequestParam(name = "urls") String[] urls){
		
		return null;
	}
}
