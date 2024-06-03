package com.compare.products.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.compare.products.models.Publication;
import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "servicio-productos-comparacion-caracteristicas")
public interface CaracteristicasClient {

	@PostMapping(value = "/compare/characteristics")
	public ResponseEntity<JsonNode> getComparasionToCharacteristics(
					@RequestBody List<Publication> publications);
}
