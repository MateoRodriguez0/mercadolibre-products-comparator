package com.products.compare.vendedores.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.products.compare.vendedores.models.Seller;
import com.products.compare.vendedores.services.SellerServices;



@RestController()
public class SellersController {

	
	@GetMapping(value = "/seller/{userId}",headers = {"Authorization"})
	public Seller searhcInfoBySeller(@PathVariable(name = "userId") String id,@RequestParam(name = "item")String itemid) {
		return sellerServices.getSellerById(id,itemid);
	}
	
	@Autowired
	private SellerServices sellerServices;
	
}
