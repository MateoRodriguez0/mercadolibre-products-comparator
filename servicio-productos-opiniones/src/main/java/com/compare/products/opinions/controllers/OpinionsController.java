package com.compare.products.opinions.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compare.products.opinions.models.PublicationType;
import com.compare.products.opinions.services.OpinionServiceFacade;

@RestController
@RequestMapping(value = "/reviews")
public class OpinionsController {
	
	@GetMapping(value="/all")
	public ResponseEntity<?> getOpinios(@RequestParam(name = "id")String id,
			@RequestParam(name = "type")PublicationType type,
			@RequestHeader(name = "Authorization") String token) {
	
		return ResponseEntity.ok(opinionServiceFacade
						.getOpinionService(type).getReviews(id,token));
	}
	
	@GetMapping(value="/rating",headers = {"Authorization"})
	public ResponseEntity<?> getRatingAverage(@RequestParam(name = "id")String id,
			@RequestParam(name = "type")PublicationType type) {
	
		return ResponseEntity.ok(opinionServiceFacade
						.getOpinionService(type).getRatingAverage(id));
	}
	
	
	
	@Autowired
	private OpinionServiceFacade opinionServiceFacade;
	
	
}
