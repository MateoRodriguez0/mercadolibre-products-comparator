package com.compare.products.opinions.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compare.products.opinions.models.PublicationType;
import com.compare.products.opinions.services.OpinionServiceFacade;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/reviews")
public class OpinionsController {
	
	@GetMapping(value="/all", headers = {"Authorization"})
	public ResponseEntity<?> getOpinios(@RequestParam(name = "id")String id,
			@RequestParam(name = "type")PublicationType type) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		return ResponseEntity.ok(opinionServiceFacade
						.getOpinionService(type).getReviews(id,token));
	}
	
	@GetMapping(value="/rating")
	public ResponseEntity<?> getRatingAverage(@RequestParam(name = "id")String id,
			@RequestParam(name = "type")PublicationType type,
			@RequestParam(required = false) boolean isparent,
			@RequestHeader(name = "Authorization") String token) {
		
		return ResponseEntity.ok(opinionServiceFacade
						.getOpinionService(type).getRatingAverage(id));
	}
	
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private OpinionServiceFacade opinionServiceFacade;
	
	
}
