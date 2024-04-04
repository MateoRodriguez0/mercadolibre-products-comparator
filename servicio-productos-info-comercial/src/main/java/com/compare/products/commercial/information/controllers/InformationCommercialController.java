package com.compare.products.commercial.information.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.compare.products.commercial.information.models.PublicationType;
import com.compare.products.commercial.information.models.Shipping;
import com.compare.products.commercial.information.services.InformationCommercialFacade;
import com.compare.products.commercial.information.services.ShippingService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class InformationCommercialController {

	
	@PostMapping(value = "/information/commercial",headers = {"Authorization"})
	public ResponseEntity<?> getInformationCommercial(@RequestBody JsonNode jsonNode,
			@RequestParam PublicationType type){
		try {
			return ResponseEntity
					.ok(commercialService.getInfoCommercial(jsonNode,type));
			
		} catch (HttpClientErrorException.Unauthorized e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(e.getMessage());
	
		}catch (HttpClientErrorException.BadRequest e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
	
	
	}
	
	@GetMapping(value = "/test/{itemId}",headers = {"Authorization"})
	public Shipping service2(@PathVariable String itemId) {
		
		return service.getShippingItem(itemId);
	}
	
	
	@Autowired
	private ShippingService service;
	
	@Value("${json.properties.item.id}")
	private String itemId;
	@Autowired
	private InformationCommercialFacade commercialService;
	
	
}
