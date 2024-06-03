package com.compare.products.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compare.products.models.PublicationComparative;
import com.compare.products.services.ComparePublication;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping(value = "/compare")
public class ComparePublicationsController {

	@GetMapping(value = "/")
	public String getconfig() {
	 return c;	
	}
	 
	  
	@GetMapping(value = "/products", headers = "Authorization")
	public ResponseEntity<?> getComparaisonByUrlsOfPublications(
				@RequestParam(name = "urls") String[] urls){
		
		Object body= comparePublication.comparisonAttempt(urls);
		if(body instanceof PublicationComparative) {
			return ResponseEntity.ok(body);
		}
		ObjectNode node= new ObjectNode(JsonNodeFactory.instance);
		node.put("status", "200");
		node.putPOJO("message", body);
		
		return ResponseEntity.ok(node);
	}
	
	@Autowired
	private ComparePublication comparePublication;
	
	@Value("${config.prueba}")
	private String c;
}

