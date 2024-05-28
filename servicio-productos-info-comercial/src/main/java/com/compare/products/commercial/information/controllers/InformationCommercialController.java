package com.compare.products.commercial.information.controllers;

import java.util.concurrent.CompletionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.compare.products.commercial.information.models.PublicationType;
import com.compare.products.commercial.information.services.InformationCommercialFacade;
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
		}catch(CompletionException e) {
			if(e.getCause().getClass()==HttpClientErrorException.Unauthorized.class) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(e.getCause().getMessage());
			}
			
		}
		return ResponseEntity.internalServerError().build();
	}
	
	@Autowired
	private InformationCommercialFacade commercialService;
}
