package com.mercadolibre.productscomparator.url.analisis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.productscomparator.url.analisis.models.ItemDetails;
import com.mercadolibre.productscomparator.url.analisis.services.AnalisisUrlService;
import com.mercadolibre.productscomparator.url.analisis.util.UrlValidator;

/**
 * Controlador rest obtener los detalles un producto correspondiente a una url.
 * 
 * @Author Mateo Rodrigez c.
 * 12 feb. 2024 4:44:45 p. m.
 */
@RestController
public class AnalisisUrlController {

	/**
	 * Analisa la url de un articulo publicado en MercadoLibre, busca
	 * y devuelve el id del item o del producto de catalogo encontrado en la url.
	 * 
	 * @param url Es la direccion del producto en MercadoLibre.
	 * @return Devuelve un Objeto ItemDetails Correspondiene a la url recibida como parametro.
	 */
	@GetMapping(value="/searchcode")
	public ResponseEntity<ItemDetails> getCodeForUrl(@RequestParam(name = "url")String url) {
		ItemDetails details=analisisUrlServiceient.CreateDetailsUrl(url);
		if(details!=null) {
			return ResponseEntity
	        		.ok(details);
		}
		return ResponseEntity
	    			.status(HttpStatus.NOT_ACCEPTABLE)
	    			.body(null);	
		}
	
	 @Autowired
	 private AnalisisUrlService analisisUrlServiceient;

	
}
