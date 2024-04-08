package com.compare.products.commercial.information.services.implementations;




import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.compare.products.commercial.information.models.CommercialInformation;
import com.compare.products.commercial.information.models.Shipping;
import com.compare.products.commercial.information.models.ShippingMode;
import com.compare.products.commercial.information.models.Warranty;
import com.compare.products.commercial.information.services.InformationCommercialService;
import com.compare.products.commercial.information.services.PaymentMethodsService;
import com.compare.products.commercial.information.services.ScrapingService;
import com.compare.products.commercial.information.services.ShippingService;
import com.fasterxml.jackson.databind.JsonNode;


@Service
@Scope("prototype")
public class InformationProductCatalog implements InformationCommercialService {
	
	@Override
	public CommercialInformation getInfoCommercial(JsonNode jsonNode,String token) {
			CommercialInformation information=new CommercialInformation();
			ExecutorService service= Executors.newVirtualThreadPerTaskExecutor();
			
			CompletableFuture<Document> taskPermalink=CompletableFuture.supplyAsync(() ->
				 scrapingService.getDocument(jsonNode.at(permalink).asText()),service);
			
			CompletableFuture<Double> taskScraperReviews=CompletableFuture.supplyAsync(() ->
				scrapingService.getRatingAverage(jsonNode.at(parentId).asText()),service);
		
			CompletableFuture<Shipping> taskShipping=CompletableFuture.supplyAsync(() ->
				getShipping(jsonNode,token),service);
			
			CompletableFuture<String> taskPayments=CompletableFuture.supplyAsync(() ->
				paymentMethodsService.findPaymentMethods(token,jsonNode.at(siteId).asText())
				,service);
			
			CompletableFuture<Double> taskRating=CompletableFuture.supplyAsync(() ->
				getRatingAverage(jsonNode.at(itemId).asText(),token),service);
			
			CompletableFuture<String> taskAvailables=CompletableFuture.supplyAsync(() ->
				scrapingService.getAvailables(taskPermalink),service);
			
			CompletableFuture<String> taskTotalSales=CompletableFuture.supplyAsync(() ->
				scrapingService.getSales(taskPermalink),service);
			
			
			CompletableFuture.runAsync(() ->{
				information.setDiscount_porcentage(getDiscount(jsonNode));
				information.setPrice(jsonNode.at(ProductPrice).asDouble());
				information.setCurrency_id(jsonNode.at(currency).asText());
				information.setInternational_delivery_mode(jsonNode.at(international).asText());
				},service); 
			
			CompletableFuture.runAsync(() ->{
				for (JsonNode atr : jsonNode.get(attributes)) {
					if(atr.get("id").asText().equals(brandId)) {
						information.setBrand(atr.at(brandName).asText());
						break;
						}
					}
				});
			
			CompletableFuture<Warranty> taskWarranty=CompletableFuture
						.supplyAsync(() -> getWarranty(jsonNode),service);

			
			
			try {
				information.setShipping(taskShipping.get());
				information.setPayment_methods(taskPayments.get());
				information.setTotal_sales(taskTotalSales.get().length()!=0 ?
						taskTotalSales.get() : null);
				information.setAvailables(taskAvailables.get().length()!=0 ?
						taskAvailables.get() : null);
				information.setWarranty(taskWarranty.get());
				
				if(information.getShipping()!=null) {
					information.getShipping().setCurrency(jsonNode.at(currency).asText());
				}
				if(taskRating.get()== 0 || taskRating.get()!= taskScraperReviews.get() 
						&& taskScraperReviews.get()!=0) {
					information.setRating_average(taskScraperReviews.get());
				}
				else {
					information.setRating_average(taskRating.get());
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		
		return information;
	
	}
	
	
	@Override
	public int getDiscount(JsonNode jsonNode) {
		double orignalPrice=jsonNode.at(ProductPriceOriginal).asInt();
		double currentPrice=jsonNode.at(ProductPrice).asInt();
		if(orignalPrice!=0) {
			return (int)(((orignalPrice-currentPrice)/orignalPrice)*100);
		}
		return 0;		
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
		if(jsonNode.at(freshipping).asBoolean()) {
			return Shipping.builder().mode(ShippingMode.free).build();
		}
		return shippingService.getShippingItem(jsonNode.at(itemId).asText(),token);
	}
	
	@Override
	public double getRatingAverage(String id, String token) {
		RequestEntity<?> entity= RequestEntity.get(ratingUrl.replace("{id}", id))
				.header("Authorization", token)
				.build();
		
		ResponseEntity<JsonNode> response=clientHttp.exchange(entity, JsonNode.class);
		
		return response.getBody().at(rating).asDouble();
	}
	
	
	
	
	
	@Autowired
	private ScrapingService scrapingService;

	@Value("${compare.products.scraper.tags.quantity-available}")
	private String availableTag;
	
	@Value("${compare.products.scraper.tags.review-calification}")
	private String calificationTag;
	
	@Value("${compare.products.scraper.tags.sales}")
	private String salesTag;
	
	@Value("${compare.products.paths.rating-for-item}")
	private String ratingUrl;
	
	@Value("${compare.products.paths.scraper.reviews}")
	private String reviewsUrl;
	
	@Value("${json.properties.product_catalog.price}")
	private String ProductPrice;
	
	@Value("${json.properties.product_catalog.original_price}")
	private String ProductPriceOriginal;

	@Value("${json.properties.product_catalog.id}")
	private String itemId;
	
	@Value("${json.properties.product_catalog.site_id}")
	private String siteId;
	
	@Value("${json.properties.product_catalog.parent}")
	private String parentId;

	@Value("${json.properties.reviews-item.rating}")
	private String rating;

	@Value("${json.properties.product_catalog.curency_id}")
	private String currency;
	
	@Value("${json.properties.product_catalog.warranty}")
	private String saleTerms;

	@Value("${json.properties.product_catalog.permalink}")
	private String permalink;
	
	@Value("${json.properties.product_catalog.name-terminos-venta}")
	private String saleTermsNameWarranty;
	
	@Value("${json.properties.product_catalog.warranty-unit}")
	private String warrantyUnit;
	
	@Value("${json.properties.product_catalog.warranty-number}")
	private String warrantyNumber;
	
	@Value("${json.properties.product_catalog.free-shipping}")
	private String freshipping;
	
	@Value("${json.properties.product_catalog.international-delivery}")
	private String international;
	
	@Value("${json.properties.product_catalog.attributes}")
	private String attributes;
	
	@Value("${json.properties.product_catalog.brand_id}")
	private String brandId;
	
	@Value("${json.properties.product_catalog.brand_name}")
	private String brandName;
	
	@Autowired
	private RestTemplate clientHttp;

	@Autowired
	private PaymentMethodsService paymentMethodsService;

	@Autowired
	private ShippingService shippingService;
	
}
