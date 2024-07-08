package com.products.compare.url.analisis.clients;

import java.util.List;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.JsonNode;
/**
 * Cliente feign para realizar peticines a la api de de mercadolibre.
 * 
 * @Author Mateo Rodrigez c.
 * 20 feb. 2024 11:23:10 a. m.
 */
@FeignClient(name = "sites",
url = "https://api.mercadolibre.com",fallback = MercadoLibreSitesClientFallBack.class )
public interface MercadoLibreSitesClient {

	/**
	 * Realiza una peticion GET al recurso de paises de la API de  MercadoLibre.
	 * 
	 * @return Devuelve el listado de paises disponibles en MercadoLibre.
	 */
	@GetMapping(value = "/sites")
	List<JsonNode>getPaises();
}
