package com.compare.products.commercial.information.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.compare.products.commercial.information.models.Shipping;
import com.compare.products.commercial.information.models.ShippingCost;
import com.compare.products.commercial.information.models.ShippingMode;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Scope("prototype")
public class ShippingService {

	public Shipping getShippingItem(String id,String token){
		try(var scope=new StructuredTaskScope<>()){
			Subtask<Boolean> freeShipping=scope.fork(() ->{
				for (JsonNode node : getShippingMethods(id, token).at(channels)) {
					System.out.println(node);
					if(node.at(freeShiping).asBoolean()) {
						return true;
					}
				}
				return false;	
			});
			Subtask<List<ShippingCost>> Costs=scope.fork(() ->{
				return getShippingCost(id,token);
			});
			scope.join();
			if(freeShipping.get()) {
				return Shipping.builder().mode(ShippingMode.free)
						.handling_costs(Costs.get())
						.build();
			}
			return Shipping.builder().mode(ShippingMode.pay)
					.handling_costs(Costs.get())
					.build();
		
		} catch (Exception e) {
			return null;
		}
	}
	
	
	private List<ShippingCost> getShippingCost(String id,String token) {
		Set<String> cities= getAddressesByUser(token);
		List<ShippingCost> costs=null;
		
		if(cities!=null) {
			costs= new ArrayList<>();
			for (String city : cities) {
				try {
					JsonNode shipping=getShippingCostByCity(id, city, token);
					costs.add(new ShippingCost(shipping.at(cityName).asText()+", "+
							shipping.at(countryName).asText(),
							shipping.at(options).get(0).at(cost).asDouble(),
							shipping.at(options).get(0).at(maxTime).asText(),
							shipping.at(options).get(0).at(minTime).asText()));
				}catch(Exception error) {}	
			}
			if(costs.size()!=0) {
				return costs;
			}
		}
		return null;
	}
	
	private JsonNode getShippingMethods(String id,String token){
		RequestEntity<?> entity= RequestEntity.get(shippingUrl.replace("{id}", id))
				.header("Authorization",token).build();
		return clientHttp.exchange(entity, JsonNode.class).getBody();
	}
	
	private JsonNode getShippingCostByCity(String itemId,String cityId, String token){
		RequestEntity<?> entity= RequestEntity.get(shippingCosts.replace("{id}", itemId)
				.replace("{city}", cityId)).header("Authorization",token )
				.build();
	
		return clientHttp.exchange(entity, JsonNode.class).getBody();
	}
	
	
	private Set<String> getAddressesByUser(String token) {
		RequestEntity<?> entity= RequestEntity.get(adreeses.replace("{id}",getIdByUserToken(token)))
				.header("Authorization", token).build();
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
	

	private String getIdByUserToken(String token){
		RequestEntity<?> entity= RequestEntity.get(userMe)
				.header("Authorization",token).build();
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
	
	@Value("${json.properties.shipping-cost.time_max}")
	private String maxTime;
	
	@Value("${json.properties.shipping-items.channels}")
	private String channels;
	
	@Value("${json.properties.shipping-items.free_shipping}")
	private String freeShiping;
	
	@Value("${json.properties.shipping-cost.time_min}")
	private String minTime;
	
	@Value("${json.properties.adresses.city-id}")
	private String userCityId;
	
}
