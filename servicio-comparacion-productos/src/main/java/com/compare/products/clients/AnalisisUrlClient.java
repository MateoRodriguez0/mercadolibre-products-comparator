package com.compare.products.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.compare.products.models.ItemDetails;

@FeignClient(name = "servicio-analisis-url")
public interface AnalisisUrlClient {

	@GetMapping(value="/searchcode")
	public ResponseEntity<ItemDetails> getCodeForUrl(@RequestParam(name = "url")String url);
}
