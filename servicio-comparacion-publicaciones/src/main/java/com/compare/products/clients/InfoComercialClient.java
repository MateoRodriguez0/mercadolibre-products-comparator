package com.compare.products.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.compare.products.models.Publication;
import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "servicio-productos-comparacion-info-comercial")
public interface InfoComercialClient {
	@PostMapping(value = "/compare/info-commercial")
	public ResponseEntity<JsonNode> getComparaison(@RequestBody List<Publication> publications,
			@RequestHeader(name = "Authorization")String token);
}
