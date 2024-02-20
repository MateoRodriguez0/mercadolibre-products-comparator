package com.mercadolibre.productscomparator.vendedores.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mercadolibre.productscomparator.vendedores.models.Seller;
import com.mercadolibre.productscomparator.vendedores.services.ResponseTimeServices;
import com.mercadolibre.productscomparator.vendedores.services.SellerServices;


@RestController()
public class SellersController {

	
	@GetMapping(value = "/seller/{userId}",headers = {"Authorization"})
	public Seller searhcInfoBySeller(@PathVariable(name = "userId") String id) {
		return sellerServices.getSellerById(id);
	}

	
	@GetMapping(value = "/questions/{itemId}/response_time")
	public ObjectNode ResponseTimeByItemId(@PathVariable(name = "itemId") String id) {
		ObjectNode node= new ObjectNode(JsonNodeFactory.instance);
		node.putPOJO("response_time",  responseTime.timeOfResponseBySeller(id));
		
		return node;
	}

	
	
	@Autowired
	private SellerServices sellerServices;
	
	@Autowired
	private ResponseTimeServices responseTime;
}
