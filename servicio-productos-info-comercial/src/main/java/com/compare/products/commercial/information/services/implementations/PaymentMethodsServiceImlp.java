package com.compare.products.commercial.information.services.implementations;

import java.net.URI;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.compare.products.commercial.information.services.PaymentMethodsService;
import com.fasterxml.jackson.databind.JsonNode;


@Service
@Primary
@Scope("prototype")
public class PaymentMethodsServiceImlp implements PaymentMethodsService {

	@Override
	public String findPaymentMethods(String token, String siteId) {
	
		try(var scope= new StructuredTaskScope<>()){
			Subtask<Boolean> matchSiteId=scope.fork(() -> {
				try {
					RequestEntity<?> entity= RequestEntity.get(userMe)
							.header("Authorization",token).build();
			
					if(clientHttp.exchange(entity, JsonNode.class).getBody()
							.at("/site_id").asText().equals(siteId)) {
						return true;
					}	
				}catch (Exception e) {}
				return false;
			});
			Subtask<String> payments=scope.fork(() ->getPaymentMethodSorteds(token));
			scope.join();
			if (matchSiteId.get()) {
				return payments.get();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	
	private String getPaymentMethodSorteds(String token) {
		try{
			JsonNode node= getPaymentMethods(token).getBody();
			StringBuilder builder=new StringBuilder();
			for (JsonNode jsonNode : node) {
				if(jsonNode.get(PaymentMethodStatus).asText().equals(statusActive)) {
					builder.append(jsonNode.get(PaymentMethodName).asText()+", ");
				}
			}
			return builder.reverse().replace(0,2, "").reverse().toString();	
		}catch(HttpClientErrorException e) {}
		return null;
	}
	

	
	
	public ResponseEntity<JsonNode> getPaymentMethods(String token){
		RequestEntity<?> request= RequestEntity.get(URI.create(PaymentMethodsApi))
				.header("Authorization", token).build();
	
		return clientHttp.exchange(request, JsonNode.class);
	}
	
	
	@Autowired
	private RestTemplate clientHttp;

	@Value("${compare.products.paths.mercado-pago.payment-methods}")
	private String PaymentMethodsApi;
	
	@Value("${json.properties.payment-methods.name}")
	private String PaymentMethodName;
	
	@Value("${json.properties.payment-methods.status}")
	private String PaymentMethodStatus;

	@Value("${json.properties.payment-methods.option-active}")
	private String statusActive;
	
	@Value("${compare.products.paths.users-me}")
	private String userMe;
}
