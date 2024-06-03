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
					.salesBysellers(sellerJson.at(userCityId).asText(),id)
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
		String city= sellerJson.at(userCityAddress).asText();
		String state= sellerClients
				.getInfoByStateId(sellerJson.at(userStateAddress).asText())
				.getBody()
				.at(stateName).asText();
				
		return city.concat(", ").concat(state).concat(".");
	}
	
	public void setInfo(ObjectNode sellerJson,Seller seller) {
		JsonNode reputationNode=sellerJson.at(reputationSeller);
		
		try(var scope= new StructuredTaskScope<>()){
			scope.fork(() ->{
				setExperience(sellerJson, seller);
				return null;
			});
			scope.fork(() ->{
				seller.setId(sellerJson.at(sellerId).asText());
				seller.setNickname(sellerJson.at(sellerNickname).asText());
				seller.setPermalink(sellerJson.at(sellerPermalink).asText());
				seller.setMetrics(sellerJson.at(sellerMetrics));
				seller.setTotal_transactions(sellerJson.at(completedTransactions).asInt());;
				seller.setNegative_rating(sellerJson.at(negativeRating).asDouble());
				seller.setNeutral_rating(sellerJson.at(neutralRating).asDouble());
				seller.setPositive_rating(sellerJson.at(positiveRating).asDouble());
				return null;
			});
			scope.fork(() ->{
				if(reputationNode.at(sellerPowerStatus).asText()!="null") {
					seller.setMercadoLider_level(reputationNode.at(sellerPowerStatus).asText());
				}
				return null;
			});
			
		}
			
		
	}
	
	
	public void setExperience(ObjectNode sellerJson,Seller seller) {
		Period experience= DatesUtil.compareDates(sellerJson.at(registrationDate)
				.asText(),
				LocalDate.now().toString(),
				sellerJson.at(sellerZoneId).asText());
		
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
        		seller.setExperience(whitoutExperience);
        	
        }
        seller.setExperience(seller.getExperience()+" "+experiencePhrase);
        
        }
	

	@Value("${compare.products.experience-seller-response.without-experience}")
	private String whitoutExperience;
	
	@Value("${compare.products.api.version}")
	private String api_version;
	
	@Value("${json.properties.info-user.site_id}")
	private String userCityId;
	
	@Value("${json.properties.info-state.name}")
	private String stateName;
	
	@Value("${json.properties.reputation-seller.zone-id}")
	private String sellerZoneId;
	
	@Value("${json.properties.reputation-seller.metrics}")
	private String sellerMetrics;
	
	@Value("${json.properties.reputation-seller.registration_date}")
	private String registrationDate;

	@Value("${json.properties.reputation-seller.transactions-completed}")
	private String completedTransactions;
	
	@Value("${json.properties.reputation-seller.reputation}")
	private String reputationSeller;
	
	@Value("${json.properties.reputation-seller.negative-rating}")
	private String negativeRating;
	
	@Value("${json.properties.reputation-seller.positive-rating}")
	private String positiveRating;
	
	@Value("${json.properties.reputation-seller.neutral-rating}")
	private String neutralRating;
	
	@Value("${json.properties.reputation-seller.power_seller_status}")
	private String sellerPowerStatus;
	
	@Value("${json.properties.reputation-seller.id}")
	private String sellerId;
	
	@Value("${json.properties.reputation-seller.nickname}")
	private String sellerNickname;

	@Value("${json.properties.reputation-seller.permalink}")
	private String sellerPermalink;

	@Value("${json.properties.user.address.state}")
	private String userStateAddress;
	
	@Value("${json.properties.user.address.city}")
	private String userCityAddress;

	@Value("${compare.products.experience-seller-response.phrase}")
	private String experiencePhrase;
	
	
	
	@Autowired
	private SellerFeignClient sellerClients;
	
	@Autowired
	private AuthorizathionService authorizathionService;
	
	@Autowired
	private ResponseTimeServices responseTime;
	
	private final WebRequest request;
	
	
	
	
	
}
