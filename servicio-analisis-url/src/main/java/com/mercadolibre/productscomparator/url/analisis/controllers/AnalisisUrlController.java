package com.mercadolibre.productscomparator.url.analisis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.productscomparator.url.analisis.models.UrlDetails;
import com.mercadolibre.productscomparator.url.analisis.services.AnalisisUrlService;

/**
 * Controlador rest obtener los detalles un producto correspondiente a una url.
 * 
 * @Author Mateo Rodrigez c.
 * 12 feb. 2024 4:44:45 p. m.
 */
@RestController
public class AnalisisUrlController {

	
	@GetMapping(value="/searchcode")
	public ResponseEntity<UrlDetails> getCodeForUrl(@RequestParam(name = "url")String url) {
		if(url.startsWith("https")) {
			return ResponseEntity
					.ok(analisisUrlServiceient.CreateDetailsUrl(url));
		}
		return ResponseEntity
				.status(HttpStatus.NOT_ACCEPTABLE)
				.body(null);	
	}
	
	 @Autowired
	 private AnalisisUrlService analisisUrlServiceient;

	
}
