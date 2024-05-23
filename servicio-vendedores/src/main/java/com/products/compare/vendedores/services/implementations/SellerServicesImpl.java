package com.products.compare.vendedores.services.implementations;


import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.products.compare.vendedores.clients.SellerFeignClient;
import com.products.compare.vendedores.models.Seller;
import com.products.compare.vendedores.services.AuthorizathionService;
import com.products.compare.vendedores.services.ResponseTimeServices;
import com.products.compare.vendedores.services.SellerServices;
import com.products.compare.vendedores.util.DatesUtil;


@Service
@Primary
public class SellerServicesImpl implements SellerServices {
	
	public SellerServicesImpl(WebRequest request) {
		super();
		this.request = request;
	}


	@Override
	public Seller getSellerById(String id,String[] items) {
		authorizathionService.SetToken(request.getHeader("Authorization"));
		
		Seller seller= new Seller();
		 
		try(var scope= new StructuredTaskScope<>()){
			Subtask<String> time=scope.fork(() ->responseTime
					.timeOfResponseBySeller(items));
			Subtask<ObjectNode> taskApi1=scope.fork(()->sellerClients.userById(id).getBody());
		
			
			scope.join();
			ObjectNode sellerJson=taskApi1.get();
			
			ObjectNode SalesForsellerJson=sellerClients
					.salesBysellers(sellerJson.get("site_id").asText(),id)
					.getBody();
					
			Subtask<String> location=scope.fork(() ->setLocation(sellerJson));
			
			scope.fork(() -> {
				setInfo(SalesForsellerJson,seller);
				return null;
			});
			scope.join();
			seller.setLocation(location.get());
			seller.setResponse_time(time.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
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
		
		try(var scope= new StructuredTaskScope<>()){
			scope.fork(() ->{
				setExperience(sellerJson, seller);
				return null;
			});
			scope.fork(() ->{
				seller.setId(sellerNode.get("id").asText());
				seller.setNickname(sellerNode.get("nickname").asText());
				seller.setPermalink(sellerNode.get("permalink").asText());
				seller.setMetrics(metricsNode);
				seller.setTotal_transactions(reputationNode.get("transactions").get("completed").asInt());;
				seller.setNegative_rating(ratingNode.get("negative").asDouble());
				seller.setNeutral_rating(ratingNode.get("neutral").asDouble());
				seller.setPositive_rating(ratingNode.get("positive").asDouble());
				return null;
			});
			scope.fork(() ->{
				if(reputationNode.get("power_seller_status").asText()!="null") {
					seller.setMercadoLider_level(reputationNode.get("power_seller_status").asText());
				}
				return null;
			});
			
		}
			
		
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
        seller.setExperience(seller.getExperience()+" vendiendo en Mercado Libre");
        
        }
	

	@Value("${compare.products.api.version}")
	private String api_version;
	@Autowired
	private SellerFeignClient sellerClients;
	
	@Autowired
	private AuthorizathionService authorizathionService;
	
	@Autowired
	private ResponseTimeServices responseTime;
	
	private final WebRequest request;
	
	
	
	
	
}
