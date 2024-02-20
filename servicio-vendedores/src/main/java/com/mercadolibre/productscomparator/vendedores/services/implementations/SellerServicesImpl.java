package com.mercadolibre.productscomparator.vendedores.services.implementations;


import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mercadolibre.productscomparator.vendedores.clients.SellerFeignClient;
import com.mercadolibre.productscomparator.vendedores.models.Seller;
import com.mercadolibre.productscomparator.vendedores.services.AuthorizathionService;
import com.mercadolibre.productscomparator.vendedores.services.SellerServices;
import com.mercadolibre.productscomparator.vendedores.util.DatesUtil;

import lombok.AllArgsConstructor;

@Service
@Primary
@AllArgsConstructor
public class SellerServicesImpl implements SellerServices {
	
	@Override
	public Seller getSellerById(String id) {
		authorizathionService.SetToken(request.getHeader("Authorization"));
		
		ObjectNode sellerJson=sellerClients.userById(id).getBody(); 
		Seller seller= Seller.builder().location(setLocation(sellerJson)).build();
		ObjectNode SalesForsellerJson=sellerClients
				.salesBysellers(sellerJson.get("site_id").asText(),id)
				.getBody(); 
		setInfo(SalesForsellerJson,seller);
		return seller;
	}
	
	
	public String setLocation(ObjectNode sellerJson) {
		String city= sellerJson.get("address").get("city").asText();
		String state= sellerClients
				.getInfoByStateId(sellerJson.get("address").get("state").asText())
				.getBody()
				.get("name").asText();
				
		return city.concat(", ").concat(state).concat(".");
	}
	
	public void setInfo(ObjectNode sellerJson,Seller seller) {
		JsonNode sellerNode=sellerJson.get("seller");
		JsonNode reputationNode=sellerNode.get("seller_reputation");
		JsonNode metricsNode =reputationNode.get("metrics");
		JsonNode ratingNode =reputationNode.get("transactions").get("ratings");
		
		seller.setId(sellerNode.get("id").asText());
		seller.setNickname(sellerNode.get("nickname").asText());
		seller.setPermalink(sellerNode.get("permalink").asText());
		seller.setMetrics(metricsNode);
		seller.setTotal_transactions(reputationNode.get("transactions").get("completed").asInt());;
		seller.setNegative_rating(ratingNode.get("negative").asDouble());
		seller.setNeutral_rating(ratingNode.get("neutral").asDouble());
		seller.setPositive_rating(ratingNode.get("positive").asDouble());
		
		if(reputationNode.get("power_seller_status").asText()!="null") {
			seller.setMercadoLider_level(reputationNode.get("power_seller_status").asText());
		}
		setExperience(sellerJson, seller);
		
		
	}
	
	
	public void setExperience(ObjectNode sellerJson,Seller seller) {
		Period experience= DatesUtil.compareDates(sellerJson.get("seller").get("registration_date")
				.asText(),
				LocalDate.now().toString(),
				sellerJson.get("country_default_time_zone").asText());
		
        int years = experience.getYears();
        int months = experience.getMonths();
        int days = experience.getDays();
		
        if(years>=1) {
        	if(years==1)
        		seller.setExperience(years+" año");
        	else
        		seller.setExperience(years+" años");
        }
        
        else if(years==0&& months>=1) {
        	if(months==1)
        		seller.setExperience(months+" mes");
        	else
        		seller.setExperience(months+" meses");
        }
        else {
        	if(days >1 )
        		seller.setExperience(days+" dias");
        	else
        		seller.setExperience("nuevo");
        	
        }
        
        
        }
	

	
	@Autowired
	private SellerFeignClient sellerClients;
	
	@Autowired
	private AuthorizathionService authorizathionService;
	

	private final WebRequest request;
	
	
	
	
}
