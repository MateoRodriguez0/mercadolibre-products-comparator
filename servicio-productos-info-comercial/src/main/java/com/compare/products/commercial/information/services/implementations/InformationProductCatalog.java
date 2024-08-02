package com.compare.products.commercial.information.services.implementations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;

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
public class InformationProductCatalog implements InformationCommercialService {
	
	@Override
	public CommercialInformation getInfoCommercial(JsonNode jsonNode) {
		String token = request.getHeader("Authorization");
		CommercialInformation information = new CommercialInformation();
		ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

		CompletableFuture<Document> taskPermalink = CompletableFuture.supplyAsync(() -> 
			scrapingService.getDocument(jsonNode.at(permalink).asText()), service);

		CompletableFuture<Shipping> taskShipping = CompletableFuture.supplyAsync(() -> 
			getShipping(jsonNode, token),service);

		CompletableFuture<String> taskPayments = CompletableFuture.supplyAsync(() -> 
			paymentMethodsService.findPaymentMethods(token, jsonNode.at(siteId).asText()), service);

		CompletableFuture<Double> taskRating = CompletableFuture.supplyAsync(() -> 
			getRating(jsonNode, token), service);

		CompletableFuture<String> taskAvailables = CompletableFuture
				.supplyAsync(() -> scrapingService.getAvailables(taskPermalink), service);

		CompletableFuture<String> taskTotalSales = CompletableFuture
				.supplyAsync(() -> scrapingService.getSales(taskPermalink), service);

		CompletableFuture.runAsync(() ->{
			information.setDiscount_porcentage(getDiscount(jsonNode));
			information.setName(jsonNode.at("/name").asText());
			information.setPermalink(jsonNode.at("/permalink").asText());
			information.setPicture(getPicture(jsonNode));
			information.setPrice(jsonNode.at(ProductPrice).asDouble());
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
			information.setShipping(taskShipping.get());
			information.setPayment_methods(taskPayments.get());
			information.setTotal_sales(taskTotalSales.get().length()!=0 ?
					taskTotalSales.get() : null);
			information.setAvailables(taskAvailables.get().length()!=0 ?
					taskAvailables.get() : null);
			information.setWarranty(taskWarranty.get());
			information.setRating_average(taskRating.get());
				
			if(information.getShipping()!=null) {
				information.getShipping().setCurrency(jsonNode.at(currency).asText());
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return information;
	
	}
	
	private Double getRating(JsonNode jsonNode, String token) {
		try(var scope= new StructuredTaskScope.ShutdownOnSuccess<Double>()){
			scope.fork(() -> {
				if(jsonNode.at(childrensId).size()!=0) {
					return getRatingAverage(jsonNode.at(id).asText(),
							PublicationType.catalog_product, true, token);
				}
				throw new RuntimeException();
			});
			scope.fork(() -> {
				if(!jsonNode.at(parentId).asText().equals("null")) {
					return getRatingAverage(jsonNode.at(parentId).asText(),
							PublicationType.catalog_product, true, token);
				}
				throw new RuntimeException();
			});
			
			scope.fork(() -> {
				if(jsonNode.at(parentId).asText().equals("null")&&jsonNode.at(childrensId).size()==0) {
					return getRatingAverage(jsonNode.at(itemId).asText(),
							PublicationType.catalog_product, false, token);
				}
				throw new RuntimeException();
			});
			scope.join();
			return scope.result();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@Override
	public JsonNode getPicture(JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return jsonNode.at("/pictures").get(0);
	}

	
	private Double getRatingAverage(String id,PublicationType type,boolean isparent,String token) {
		return client.getRatingAverage(id, type, isparent, token).getBody();
		
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
		return shippingService.getShippingItem(jsonNode.at(itemId).asText(),token);
	}
	
	@Override
	public double getRatingAverage(String id, String token) {
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
	
	
	@Autowired
	private ScrapingService scrapingService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private RatingClient client;

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
	private String id;
	
	@Value("${json.properties.product_catalog.item_id}")
	private String itemId;
	
	@Value("${json.properties.product_catalog.site_id}")
	private String siteId;
	
	@Value("${json.properties.product_catalog.parent}")
	private String parentId;
	
	@Value("${json.properties.product_catalog.children_ids}")
	private String childrensId;

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
