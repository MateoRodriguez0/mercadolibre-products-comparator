package com.mercadolibre.productscomparator.url.analisis.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.productscomparator.url.analisis.models.UrlDetails;
import com.mercadolibre.productscomparator.url.analisis.services.AnalisisUrlService;


@RestController
public class AnalisisUrlController {

	
	@GetMapping(value="/searchcode")
	public ResponseEntity<UrlDetails> getCodeForUrl(@RequestParam(name = "url")String url) {
		return ResponseEntity.ok(analisisUrlServiceient.CreateDetailsUrl(url));
	}
	
	
	
	 @Autowired
	 private AnalisisUrlService analisisUrlServiceient;
	
	
}
