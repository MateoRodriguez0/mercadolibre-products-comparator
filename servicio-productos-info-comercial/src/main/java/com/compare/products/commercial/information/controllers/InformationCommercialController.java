package com.compare.products.commercial.information.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.compare.products.commercial.information.models.PublicationType;
import com.compare.products.commercial.information.services.InformationCommercialService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class InformationCommercialController {

	
	@PostMapping(value = "/information/commercial")
	public ResponseEntity<?> getInformationCommercial(@RequestBody JsonNode jsonNode,
			@RequestParam PublicationType type, @RequestHeader(name = "Authorization") String token){
		try {
			return ResponseEntity
					.ok(commercialService.getInfoCommercial(jsonNode, token,type));
			
		} catch (HttpClientErrorException.Unauthorized e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(e.getMessage());
	
		}catch (HttpClientErrorException.BadRequest e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	
	
	}
	
	
	
	@Autowired
	private InformationCommercialService commercialService;
	
	
}
