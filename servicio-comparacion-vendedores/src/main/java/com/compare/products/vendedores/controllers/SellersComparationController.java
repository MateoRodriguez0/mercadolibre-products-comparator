package com.compare.products.vendedores.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.compare.products.vendedores.models.SellerCompare;
import com.compare.products.vendedores.services.ComparationService;

@RestController
public class SellersComparationController {

	@PostMapping(value = "/compare/sellers")
	private ResponseEntity<?> comparesellers(@RequestBody List<SellerCompare> sellers,
			@RequestHeader(name = "Authorization") String token){
		
		try {
			return ResponseEntity.ok(comparationService.compare(sellers));
		} catch (InterruptedException e) {
			return ResponseEntity.noContent().build();
		}
	}
	
	@Autowired
	private ComparationService comparationService;
	
}
