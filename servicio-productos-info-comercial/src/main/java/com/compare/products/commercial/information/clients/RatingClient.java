package com.compare.products.commercial.information.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.compare.products.commercial.information.models.PublicationType;

@FeignClient(name = "servicio-productos-opiniones", url = "localhost:8084")
public interface RatingClient {
	
	@GetMapping(value="/reviews/rating")
	public ResponseEntity<Double> getRatingAverage(@RequestParam(name = "id")String id,
			@RequestParam(name = "type") PublicationType type,
			@RequestParam(required = false) boolean isparent,
			@RequestHeader(name = "Authorization") String token);
}
