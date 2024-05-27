package com.compare.products.vendedores.services.implementations;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


import com.compare.products.vendedores.clients.SellerServiceClient;
import com.compare.products.vendedores.models.ComarativeInfoSellers;
import com.compare.products.vendedores.models.InfoSeller;
import com.compare.products.vendedores.models.ResponseTime;
import com.compare.products.vendedores.models.Seller;
import com.compare.products.vendedores.models.SellerCompare;
import com.compare.products.vendedores.services.ComparationService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Scope("prototype")
public class CompartionServiceImpl implements ComparationService{

	@Override
	public ComarativeInfoSellers compare(List<SellerCompare> sellers) throws InterruptedException {
		String token = request.getHeader("Authorization");
		ComarativeInfoSellers comarative=new ComarativeInfoSellers();
		
		try(var scope= new StructuredTaskScope<>()){
			for (SellerCompare seller : sellers) {
				scope.fork(() ->{
					Seller s =client.searhcInfoBySeller(seller.getId(), 
							seller.getItems(), token);
			
					
					comarative.getNickname().add(new InfoSeller(
							seller.getPublication_id(), s.getNickname()));
					comarative.getResponse_time().add(new InfoSeller(
							seller.getPublication_id(), getResponseTime(s)));
					
					comarative.getLinks().add(new InfoSeller(
							seller.getPublication_id(),s.getPermalink()));

					comarative.getMetrics().getClaims().add(new InfoSeller(
							seller.getPublication_id(),getclaims(s.getMetrics())));
					
					comarative.getMetrics().getCancellations().add(new InfoSeller(
							seller.getPublication_id(),getCancelations(s.getMetrics())));
					
					comarative.getMetrics().getDelayed_handling_time().add(new InfoSeller(
							seller.getPublication_id(),getDelays(s.getMetrics())));
					
					comarative.getSales().add(new InfoSeller(
							seller.getPublication_id(),getSales(s.getMetrics())));
					
					comarative.getExperience().add(new InfoSeller(
							seller.getPublication_id(),s.getExperience()));

					comarative.getMercadoLider_level().add(new InfoSeller(
							seller.getPublication_id(),s.getMercadoLider_level()));
					
					comarative.getNegative_rating().add(new InfoSeller(
							seller.getPublication_id(),(int)(s.getNegative_rating()*100)+"%"));

					comarative.getNeutral_rating().add(new InfoSeller(
							seller.getPublication_id(),(int)(s.getNeutral_rating()*100)+"%"));
					
					comarative.getPositive_rating().add(new InfoSeller(
							seller.getPublication_id(),(int)(s.getPositive_rating()*100)+"%"));
					
					comarative.getLocation().add(new InfoSeller(
							seller.getPublication_id(),s.getLocation()));
					return true;
				});
			}
			scope.join();
		}
		
		return comarative;
	}
	
	
	private String getResponseTime(Seller seller) {
		if(seller.getResponse_time().equals(ResponseTime.UNQUESTION.name())) {
			return "Todavia no tiene preguntas";
		}
		if(seller.getResponse_time().equals(ResponseTime.UNANSWERED.name())) {
			return "Aún no ha respondido ninguna pregunta";
		}
		return seller.getResponse_time();
	}
	
	private String getclaims(JsonNode metrics) {
		String period=metrics.at(claimsPeriod).asText().replace("days", "dias");
		String total=metrics.at(totalClaims).asText().length()!=0?
				metrics.at(totalClaims).asText(): metrics.at(claimsValue).asText();
		return total+" en los ultimos "+period;
		
	}
	
	private String getDelays(JsonNode metrics) {
		String period=metrics.at(delayPeriod).asText().replace("days", "dias");
		String total=metrics.at(totaldelays).asText().length()!=0? 
				metrics.at(totaldelays).asText():metrics.at(delayValue).asText();
		return total+" en los ultimos "+period;
		
	}
	
	private String getCancelations(JsonNode metrics) {
		String period=metrics.at(cancelationsPeriod).asText().replace("days", "dias");
		String total= metrics.at(totalCancelations).asText().length()!=0?
				metrics.at(totalCancelations).asText():metrics.at(cancelationsValue).asText();
		return total+" en los ultimos "+period;
		
	}
	
	private String getSales(JsonNode metrics) {
		String period=metrics.at(salesPeriod).asText().replace("days", "dias");
		String completed=metrics.at(salesCompleted).asText();
		return completed+" en los ultimos "+ period;
		
	}
	
	@Autowired
	private SellerServiceClient client;
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("${json.properties.metrics.claims.period}")
	private String claimsPeriod;
	@Value("${json.properties.metrics.claims.total}")
	private String totalClaims;
	@Value("${json.properties.metrics.claims.value}")
	private String claimsValue;
	
	@Value("${json.properties.metrics.sales.completed}")
	private String salesCompleted;
	@Value("${json.properties.metrics.sales.period}")
	private String salesPeriod;
	
	
	@Value("${json.properties.metrics.delayed_handling_time.period}")
	private String delayPeriod;
	@Value("${json.properties.metrics.delayed_handling_time.total}")
	private String totaldelays;
	@Value("${json.properties.metrics.delayed_handling_time.value}")
	private String delayValue;
	
	
	@Value("${json.properties.metrics.cancellations.period}")
	private String cancelationsPeriod;
	@Value("${json.properties.metrics.cancellations.total}")
	private String totalCancelations;
	@Value("${json.properties.metrics.cancellations.value}")
	private String cancelationsValue;
	

}
