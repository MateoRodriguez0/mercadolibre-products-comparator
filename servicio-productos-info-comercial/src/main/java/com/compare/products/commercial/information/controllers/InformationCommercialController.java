package com.compare.products.commercial.information.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compare.products.commercial.information.models.PublicationType;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class InformationCommercialController {

	
	@PostMapping(value = "/information/commercial",headers = "Authorization")
	public ResponseEntity<?> getInformationCommercial(@RequestBody JsonNode jsonNode,
			@RequestParam PublicationType type ){
		System.out.println(jsonNode.get("attributes"));
		
		return null;
	}
	
}
