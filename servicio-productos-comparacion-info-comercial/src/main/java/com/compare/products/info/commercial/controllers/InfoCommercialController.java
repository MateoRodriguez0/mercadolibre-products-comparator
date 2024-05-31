package com.compare.products.info.commercial.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.compare.products.info.commercial.models.Publication;
import com.compare.products.info.commercial.services.ComparationServices;

@RestController
public class InfoCommercialController {

	@PostMapping(value = "/compare/info-commercial",headers = "Authorization")
	public ResponseEntity<?> getComparaison(@RequestBody List<Publication> publications){
		return ResponseEntity.ok(comparationServices.compare(publications));
	}
	
	@Autowired
	private ComparationServices comparationServices;
}
