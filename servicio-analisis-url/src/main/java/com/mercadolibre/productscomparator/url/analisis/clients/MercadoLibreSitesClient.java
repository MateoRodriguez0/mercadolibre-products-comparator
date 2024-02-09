package com.mercadolibre.productscomparator.url.analisis.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.JsonNode;


@FeignClient(name = "sites",url = "https://api.mercadolibre.com",
			fallback = MercadoLibreSitesClientFallBack.class )
public interface MercadoLibreSitesClient {

	@GetMapping(value = "/sites")
	List<JsonNode>getPaises();
}
