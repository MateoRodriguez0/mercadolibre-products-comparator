package com.compare.products.vendedores.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.compare.products.vendedores.models.Seller;


@FeignClient(name = "servicio-vendedores")
public interface SellerServiceClient {

	@GetMapping(value = "/seller/{userId}")
	public Seller searhcInfoBySeller(@PathVariable(name = "userId") String id,
			@RequestParam(name = "items") String [] items,
			@RequestHeader("Authorization") String token);
	
}
