package com.compare.products.info.commercial.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.compare.products.commons.models.CommercialInformation;
import com.compare.products.commons.models.PublicationType;
import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "InfoCommercialClient", url = "http://localhost:9091")
public interface InfoCommercialClient {

	@PostMapping(value = "/information/commercial")
	public ResponseEntity<CommercialInformation> getInformationCommercial(
			@RequestBody JsonNode jsonNode,
			@RequestParam PublicationType type,
			@RequestHeader(name = "Authorization") String token);
}