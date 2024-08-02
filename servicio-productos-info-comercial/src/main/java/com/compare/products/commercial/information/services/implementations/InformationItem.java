package com.compare.products.commercial.information.services.implementations;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.compare.products.commercial.information.clients.RatingClient;
import com.compare.products.commercial.information.models.CommercialInformation;
import com.compare.products.commercial.information.models.PublicationType;
import com.compare.products.commercial.information.models.Shipping;
import com.compare.products.commercial.information.models.Warranty;
import com.compare.products.commercial.information.services.InformationCommercialService;
import com.compare.products.commercial.information.services.PaymentMethodsService;
import com.compare.products.commercial.information.services.ScrapingService;
import com.compare.products.commercial.information.services.ShippingService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Scope("prototype")
@RefreshScope
public class InformationItem implements InformationCommercialService {

	@Override
	public CommercialInformation getInfoCommercial(JsonNode jsonNode) {
		String token=request.getHeader("Authorization");
		CommercialInformation information=new CommercialInformation();
		ExecutorService service= Executors.newVirtualThreadPerTaskExecutor();
		
		CompletableFuture<Document> taskPermalink=CompletableFuture.supplyAsync(() ->
			scrapingService.getDocument(jsonNode.at(permalink).asText()),service);
		CompletableFuture<String> taskAvailables=CompletableFuture.supplyAsync(() ->
			scrapingService.getAvailables(taskPermalink),service);
	
		CompletableFuture<String> taskTotalSales=CompletableFuture.supplyAsync(() ->
			scrapingService.getSales(taskPermalink),service);
		
		CompletableFuture<Shipping> taskShipping=CompletableFuture.supplyAsync(() ->
			getShipping(jsonNode,token),service);
		
		CompletableFuture<String> taskPayments=CompletableFuture.supplyAsync(() ->
			paymentMethodsService.findPaymentMethods(token, jsonNode.at(siteId).asText())
			,service);
			
		CompletableFuture<Double> taskRating=CompletableFuture.supplyAsync(() ->
			client.getRatingAverage(jsonNode.at(itemId).asText(), 
							PublicationType.item, false, token).getBody());
			
		CompletableFuture.runAsync(() ->{
			information.setDiscount_porcentage(getDiscount(jsonNode));
			information.setName(jsonNode.at("/title").asText());
			information.setPrice(jsonNode.at(ItemPrice).asDouble());
			information.setPermalink(jsonNode.at("/permalink").asText());
			information.setPicture(getPicture(jsonNode));
			information.setCurrency_id(jsonNode.at(currency).asText());
			information.setInternational_delivery_mode(jsonNode.at(international).asText());
			},service); 
			
		CompletableFuture.runAsync(() ->{
			for (JsonNode atr : jsonNode.at(attributes)) {
				if(atr.get("id").asText().equals(brandId)) {
					information.setBrand(atr.at(brandName).asText());
					break;
					}
				}
			});
			
		CompletableFuture<Warranty> taskWarranty=CompletableFuture
					.supplyAsync(() -> getWarranty(jsonNode),service);
		
		try {
			information.setTotal_sales(taskTotalSales.get().length()!=0 ?
					taskTotalSales.get() : null);
			information.setAvailables(taskAvailables.get().length()!=0 ?
						taskAvailables.get() : null);
			information.setShipping(taskShipping.get());
			information.setPayment_methods(taskPayments.get());
			information.setRating_average(taskRating.get());
			information.setWarranty(taskWarranty.get());
			
			if(information.getShipping()!=null) {
				information.getShipping().setCurrency(jsonNode.at(currency).asText());
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return information;
	}




	@Override
	public Warranty getWarranty(JsonNode jsonNode) {
		for (JsonNode term : jsonNode.at(saleTerms)) {
			if(term.get("id").asText().equals(saleTermsNameWarranty)) {
				return Warranty.builder()
						.number(term.at(warrantyNumber).asInt())
						.unit(term.at(warrantyUnit).asText())
						.build();
			}
		}
		return null;
	}

	@Override
	public Shipping getShipping(JsonNode jsonNode, String token) {
		return shippingService.getShippingItem(jsonNode.at(itemId).asText(),token);	
	}


	@Override
	public int getDiscount(JsonNode jsonNode) {
		int orignalPrice=jsonNode.at(ItemPriceOriginal).asInt();
		int currentPrice=jsonNode.at(ItemPrice).asInt();
		if(orignalPrice!=0) {
			return (int)(((double)(orignalPrice-currentPrice)/orignalPrice)*100);
		}
		return 0;
			
	}
	
	@Override
	public double getRatingAverage(String id,String token) {
		RequestEntity<?> entity= RequestEntity.get(ratingUrl.replace("{id}", id))
				.header("Authorization",token )
				.build();
		try {
			ResponseEntity<JsonNode> response=clientHttp.exchange(entity, JsonNode.class);
			return response.getBody().at(rating).asDouble();
		}catch(Exception e) {
			return 0;
		}
	}
	
	@Override
	public JsonNode getPicture(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return jsonNode.at("/pictures").get(0);
	}

	
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ScrapingService scrapingService;
	
	@Autowired
	private ShippingService shippingService;

	@Autowired
	private RatingClient client;
	
	@Value("${compare.products.paths.rating-for-item}")
	private String ratingUrl;
	
	@Value("${compare.products.paths.scraper.reviews}")
	private String reviewsUrl;
	
	@Value("${json.properties.item.free-shipping}")
	private String freshipping;
	 
	@Value("${compare.products.scraper.tags.quantity-available}")
	private String availableTag;
	
	@Value("${compare.products.scraper.tags.review-calification}")
	private String calificationTag;
	
	@Value("${compare.products.scraper.tags.sales}")
	private String salesTag;

	@Value("${json.properties.item.price}")
	private String ItemPrice;
	
	@Value("${json.properties.item.original_price}")
	private String ItemPriceOriginal;

	@Value("${json.properties.item.id}")
	private String itemId;
	
	@Value("${json.properties.item.site_id}")
	private String siteId;
	
	@Value("${json.properties.reviews-item.rating}")
	private String rating;
	
	@Value("${json.properties.item.curency_id}")
	private String currency;
	
	@Value("${json.properties.item.attributes}")
	private String attributes;
	@Value("${json.properties.item.brand_id}")
	private String brandId;
	
	@Value("${json.properties.item.brand_name}")
	private String brandName;
	
	@Value("${json.properties.item.warranty}")
	private String saleTerms;
	
	@Value("${json.properties.item.name-terminos-venta}")
	private String saleTermsNameWarranty;

	@Value("${json.properties.item.international-delivery}")
	private String international;
		
	@Value("${json.properties.item.warranty-unit}")
	private String warrantyUnit;
	
	@Value("${json.properties.item.warranty-number}")
	private String warrantyNumber;
	
	@Value("${json.properties.item.permalink}")
	private String permalink;
	
	@Autowired
	private RestTemplate clientHttp;

	@Autowired
	private PaymentMethodsService paymentMethodsService;

	
}
