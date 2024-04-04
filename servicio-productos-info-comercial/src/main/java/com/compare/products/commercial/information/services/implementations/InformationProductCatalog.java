package com.compare.products.commercial.information.services.implementations;



import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.compare.products.commercial.information.services.ShippingService;
import com.fasterxml.jackson.databind.JsonNode;


import jakarta.servlet.http.HttpServletRequest;

@Service
public class InformationProductCatalog implements InformationCommercialService {
	
	@Override
	public CommercialInformation getInfoCommercial(JsonNode jsonNode) {
		CommercialInformation information = new CommercialInformation();
		
		information.setRating_average(getRatingAverage(jsonNode.at(itemId).asText()));
		information.setPayment_methods(paymentMethodsService.findPaymentMethods());
		information.setCurrency_id(jsonNode.at(currency).asText());
		information.setRating_average(getRatingAverage(jsonNode.at(itemId).asText()));
		information.setDiscount_porcentage(getDiscount(jsonNode));
		information.setWarranty(getWarranty(jsonNode));
		information.setShipping(getShipping(jsonNode));
		information.setPrice(jsonNode.at(ProductPrice).asDouble());
		information.setInternational_delivery_mode(jsonNode.at(international).asText());
		if(information.getShipping().getCosts()!=null) {
			information.getShipping().setCurrency(jsonNode.at(currency).asText());
		}

		Document document =null;
		try {
			document = Jsoup.connect(jsonNode.at(permalink).asText()).get();
		} catch (IOException e) {
			
		}
		try {
			String sales=document.getElementsByClass(salesTag)
					.text()
					.replaceAll("[a-zA-Z]|\s|\\|","");
			information.setTotal_sales(sales.length()!=0 ? sales : null);
		} catch ( NumberFormatException e) {
			
		}
		try {
			String available=document.getElementsByClass(availableTag)
					.text()
					.replaceAll("[a-zA-Z]|\s|[(]|[)]","");
			information.setAvailables(available.length()!=0 ? available : null);
		} catch (NumberFormatException e) {
			
		}
		
		if(information.getRating_average()==0&& jsonNode.at(parentId)!=null) {
			try {
				document = Jsoup.connect(reviewsUrl.replace("{parent_id}",
						jsonNode.at(parentId).asText()))
						.get();
				information.setRating_average(Double.parseDouble(document.getElementsByClass("ui-review-capability__rating__average ui-review-capability__rating__average--desktop").text()));
			} catch (IOException | NumberFormatException e) {
				e.printStackTrace();
				}
		}
		
		for (JsonNode atr : jsonNode.get(attributes)) {
			if(atr.get("id").asText().equals(brandId)) {
				information.setBrand(atr.get(brandName).asText());
				break;
			}
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
	public Shipping getShipping(JsonNode jsonNode) {
		if(jsonNode.at(freshipping).asBoolean()) {
			return Shipping.builder().mode(ShippingMode.free).build();
		}
		
		return shippingService.getShippingItem(jsonNode.at(itemId).asText());
	}
	
	@Override
	public double getRatingAverage(String id) {
		RequestEntity<?> entity= RequestEntity.get(ratingUrl.replace("{id}", id))
				.header("Authorization", request.getHeader("Authorization"))
				.build();
		ResponseEntity<JsonNode> response=clientHttp.exchange(entity, JsonNode.class);
		
		return response.getBody().at(rating).asDouble();
	}
	

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
	
	@Autowired
	private HttpServletRequest request;
}
