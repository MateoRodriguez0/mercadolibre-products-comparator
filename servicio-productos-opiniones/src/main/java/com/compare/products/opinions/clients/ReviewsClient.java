package com.compare.products.opinions.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "ReviewsClient")
public interface ReviewsClient {

	@GetMapping(value = "${compare.products.paths.producto-catalogo}")
	public ResponseEntity<JsonNode> findCatalogProduct(@PathVariable(name = "id")String id,
			@RequestHeader(name = "Authorization") String token);

	@GetMapping(value = "${compare.products.paths.search-item}")
	public ResponseEntity<JsonNode> findItem(@PathVariable(name = "id")String id,
			@RequestHeader(name = "Authorization") String token);
	
	@GetMapping(value = "${compare.products.paths.reviews}")
	public ResponseEntity<JsonNode> findReviewsItem(@PathVariable(name = "id")String id,
			@RequestParam("offset")int offset, 
			@RequestHeader(name = "Authorization") String token);
}
