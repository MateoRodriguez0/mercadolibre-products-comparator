package com.compare.products.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.compare.products.models.SellerCompare;
import com.fasterxml.jackson.databind.JsonNode;


@FeignClient(name = "servicio-comparacion-vendedores")
public interface VendedoresClient {

	@PostMapping(value = "/compare/sellers")
	public ResponseEntity<JsonNode> comparesellers(@RequestBody List<SellerCompare> sellers,
			@RequestHeader(name = "Authorization") String token);
}
