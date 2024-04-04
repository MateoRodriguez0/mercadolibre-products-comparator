package com.compare.products.commercial.information.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.compare.products.commercial.information.models.Shipping;
import com.compare.products.commercial.information.models.ShippingCost;
import com.compare.products.commercial.information.models.ShippingMode;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ShippingService {

	
	public Shipping getShippingItem(String id) {
		RequestEntity<?> entity= RequestEntity.get(shippingUrl.replace("{id}", id))
				.header("Authorization", request.getHeader("Authorization")).build();
		ResponseEntity<JsonNode> response=clientHttp.exchange(entity, JsonNode.class);
		
		for (JsonNode node : response.getBody().at(channels)) {
			if(node.at(freeShiping).asBoolean()==true)
				return Shipping.builder().mode(ShippingMode.free)
						.build();
		}
		List<ShippingCost> costs= getShippingCost(id);
		if(costs!= null) {
			return Shipping.builder().mode(ShippingMode.pay)
					.costs(costs).build();
		}

		return null;
	
	}
	
	
	private List<ShippingCost> getShippingCost(String id) {
		Set<String> cities= getAddressesByUser();
		List<ShippingCost> costs=null;
		
		if(cities!=null) {
			costs= new ArrayList<>();
			for (String city : cities) {
				RequestEntity<?> entity= RequestEntity
						.get(shippingCosts.replace("{id}", id)
								.replace("{city}", city))
						.header("Authorization",request.getHeader("Authorization") )
						.build();
				try {
					ResponseEntity<JsonNode> response=clientHttp.exchange(entity, JsonNode.class);
					JsonNode shipping= response.getBody();
					
					costs.add(new ShippingCost(shipping.at(cityName).asText()+", "+
							shipping.at(countryName).asText(),
							shipping.at(options).get(0).at(cost).asDouble(),
							shipping.at(options).get(0).at(maxTime).asText(),
							shipping.at(options).get(0).at(minTime).asText()));
				}catch(HttpClientErrorException.NotFound error) {}	
			}
			if(costs.size()!=0) {
				return costs;
			}
		}
		return null;
	}
	
	
	private Set<String> getAddressesByUser() {
		RequestEntity<?> entity= RequestEntity.get(adreeses.replace("{id}",getIdByUserToken()))
				.header("Authorization", request.getHeader("Authorization")).build();
		ResponseEntity<JsonNode> response=clientHttp.exchange(entity, JsonNode.class);
		
		if(response.getStatusCode().value()==200) {
			Set<String> citys=new HashSet<>();
			for (JsonNode adress : response.getBody()) {
				citys.add(adress.at(userCityId).asText());
			}
			return citys;
		}
		
		return null;
	}
	
	private String getIdByUserToken(){
		RequestEntity<?> entity= RequestEntity.get(userMe)
				.header("Authorization", request.getHeader("Authorization")).build();
		ResponseEntity<JsonNode> response=clientHttp.exchange(entity, JsonNode.class);
		
		return response.getBody().get("id").asText();
		
	}
	
	@Autowired
	private RestTemplate clientHttp;
	
	@Value("${compare.products.paths.shipping-item}")
	private String shippingUrl;
	
	@Value("${compare.products.paths.shipping-cost}")
	private String shippingCosts;
	
	@Value("${compare.products.paths.users-me}")
	private String userMe;
	
	@Value("${json.properties.shipping-cost.costs}")
	private String cost;
	
	@Value("${json.properties.shipping-cost.options}")
	private String options;
	
	@Value("${json.properties.shipping-cost.city_name}")
	private String cityName;
	
	@Value("${json.properties.shipping-cost.country_name}")
	private String countryName;
	
	@Value("${json.properties.shipping-cost.time_min}")
	private String shippingCityId;
	
	@Value("${compare.products.paths.adresses}")
	private String adreeses;
	
	@Value("${json.properties.shipping-cost.time_min}")
	private String maxTime;
	
	@Value("${json.properties.shipping-items.channels}")
	private String channels;
	
	@Value("${json.properties.shipping-items.free_shipping}")
	private String freeShiping;
	
	@Value("${json.properties.shipping-cost.time_min}")
	private String minTime;
	
	@Value("${json.properties.adresses.city-id}")
	private String userCityId;
	
	@Autowired
	private HttpServletRequest request;

}
