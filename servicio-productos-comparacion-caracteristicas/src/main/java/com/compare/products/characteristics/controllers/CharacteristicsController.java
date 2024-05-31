package com.compare.products.characteristics.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.compare.products.characteristics.models.Publication;
import com.compare.products.characteristics.services.ComparationServices;

@RestController
public class CharacteristicsController {
	
	@PostMapping(value = "/compare/characteristics")
	public ResponseEntity<?> getComparasionToCharacteristics(
					@RequestBody List<Publication> publications){
		
		return ResponseEntity.ok(comparationServices.compare(publications));
	
	
	}
	
	@Autowired
	private ComparationServices comparationServices;

		
}
