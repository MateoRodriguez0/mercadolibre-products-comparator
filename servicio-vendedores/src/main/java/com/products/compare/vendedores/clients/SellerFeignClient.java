package com.products.compare.vendedores.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.products.compare.vendedores.config.SellersClientConfig;

@FeignClient(name = "users", configuration = SellersClientConfig.class)
public interface SellerFeignClient {

	@GetMapping(value = "/users/{id}")
	public ResponseEntity<ObjectNode> userById(@PathVariable(name = "id") String id);
	
	@GetMapping(value = "/sites/{siteId}/search")
	public ResponseEntity<ObjectNode>  salesBysellers(@PathVariable(name = "siteId") String site,
			@RequestParam(name ="seller_id")String id);
	
	@GetMapping(value = "/classified_locations/states/{state}")
	public ResponseEntity<ObjectNode>  getInfoByStateId(@PathVariable(name = "state") String state);
	
	@GetMapping(value = "/questions/search")
	public ResponseEntity<ObjectNode>  getQuestionsByItem(@RequestParam(name = "item")String itemId,
			@RequestParam(name = "api_version")String api_version,@RequestParam(name = "offset",defaultValue = "0")int offset);
	
	

}
