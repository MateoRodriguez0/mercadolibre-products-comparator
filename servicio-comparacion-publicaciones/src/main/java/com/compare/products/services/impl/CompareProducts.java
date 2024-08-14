package com.compare.products.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.clients.CaracteristicasClient;
import com.compare.products.clients.InfoComercialClient;
import com.compare.products.clients.VendedoresClient;
import com.compare.products.models.Publication;
import com.compare.products.models.PublicationComparative;
import com.compare.products.models.PublicationType;
import com.compare.products.models.SellerCompare;
import com.compare.products.services.ComparePublicationsService;
import com.compare.products.services.ProductsService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Primary
@Scope("prototype")
@RefreshScope
public class CompareProducts implements ComparePublicationsService {

	@Override
	public PublicationComparative getComparative(List<Publication> publications)
			throws Exception {
		String token= request.getHeader("Authorization");
		PublicationComparative comparative= new PublicationComparative();
		try(var scope= new StructuredTaskScope<JsonNode>()){
			Subtask<JsonNode>task1= scope.fork(() -> getComparativeCaracterists(publications));
			Subtask<JsonNode>task2= scope.fork(() -> getComparativeSellers(publications, token));
			Subtask<JsonNode>task3= scope.fork(() -> getComparativeInfoComercial(publications,
					token));
			scope.join();
			comparative.setSpecifications(task1.get());
			comparative.setInfo_sellers(task2.get());
			comparative.setInfo_comercial(task3.get());
		}
		return comparative;
	}

	private JsonNode getComparativeCaracterists(List<Publication> publs) {
		 return caracteristicasClient.getComparasionToCharacteristics(publs).getBody();
	}
	
	private JsonNode getComparativeInfoComercial(List<Publication> publs, String token) {
		 return infoComercialClient.getComparaison(publs, token).getBody();
	}
	
	private JsonNode getComparativeSellers(List<Publication> publs, String token) {
		List<SellerCompare> sellers= new ArrayList<>();
		try (var scope= new StructuredTaskScope<>()){
			for (Publication p : publs) {
				scope.fork(() -> {
					List<String> items= productsService.getItemsId(p.getPublication(), token);
					SellerCompare seller=SellerCompare.builder()
							.items(items.toArray(String[]::new))
							.publication_id(p.getPublication_id())
							.build();
					if(p.getPublicationType()==PublicationType.catalog_product) {
						seller.setId(p.getPublication().at(sellerItemProduct).asText());
					}
					else {
						seller.setId(p.getPublication().at(sellerItem).asText());
					}
					sellers.add(seller);
					return true;
				});
			}
			scope.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		if(sellers.size()!=0) {
			return vendedoresClient.comparesellers(sellers, token).getBody();
		}
		return null; 
	}

	@Autowired
	private ProductsService productsService;
	
	@Autowired
	private VendedoresClient vendedoresClient;

	@Autowired
	private InfoComercialClient infoComercialClient;
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("${json.properties.item.seller_id}")
	private String sellerItem;
	
	@Value("${json.properties.product_catalog.seller_id}")
	private String sellerItemProduct;
	
	@Autowired
	private CaracteristicasClient caracteristicasClient;
}
