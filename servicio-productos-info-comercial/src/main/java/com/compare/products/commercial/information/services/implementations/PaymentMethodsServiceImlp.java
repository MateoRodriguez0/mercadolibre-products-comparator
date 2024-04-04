package com.compare.products.commercial.information.services.implementations;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.compare.products.commercial.information.services.PaymentMethodsService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Primary
public class PaymentMethodsServiceImlp implements PaymentMethodsService {

	@Override
	public String findPaymentMethods() {
		JsonNode node= getPaymentMethods(request.getHeader("Authorization")).getBody();
		StringBuilder builder=new StringBuilder();
		for (JsonNode jsonNode : node) {
			if(jsonNode.get(PaymentMethodStatus).asText().equals(statusActive)) {
				builder.append(jsonNode.get(PaymentMethodName).asText()+", ");
			}
		}
		return builder.reverse().replace(0,2, "").reverse().toString();	
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
	
	@Autowired
	private HttpServletRequest request;
}
