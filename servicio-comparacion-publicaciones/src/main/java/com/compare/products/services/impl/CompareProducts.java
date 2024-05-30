package com.compare.products.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.clients.VendedoresClient;
import com.compare.products.models.ItemDetails;
import com.compare.products.models.PublicationComparative;
import com.compare.products.models.SellerCompare;
import com.compare.products.services.ComparePublicationsService;
import com.compare.products.services.ProductsService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Primary
@Scope("prototype")
public class CompareProducts implements ComparePublicationsService {

	@Override
	public PublicationComparative getComparative(List<JsonNode> nodes) {
		String token= request.getHeader("Authorization");
		PublicationComparative comparative= new PublicationComparative();
		comparative.setInfo_sellers(getComparativeSellers(nodes, token));
		return comparative;
	}

	
	private JsonNode getComparativeSellers(List<JsonNode> nodes, String token) {
		System.out.println(nodes.size());
		List<SellerCompare> sellers= new ArrayList<>();
		for (JsonNode jsonNode : nodes) {
			try {
				List<String> items= productsService.getItemsId(jsonNode, token);
				SellerCompare seller=SellerCompare.builder()
						.items(items.toArray(String[]::new))
						.publication_id(jsonNode.at("/id").asText())
						.build();
				if(jsonNode.at(sellerItemProduct).asText().length()!=0) {
					seller.setId(jsonNode.at(sellerItemProduct).asText());
				}
				else {
					seller.setId(jsonNode.at(sellerItem).asText());
				}
				sellers.add(seller);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		if(sellers.size()!=0) {
			return vendedoresClient.comparesellers(sellers, token).getBody();
		}
		return null; 
	}
	
	@Override
	public List<ItemDetails> getDetails(String[] urls) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Autowired
	private ProductsService productsService;
	
	@Autowired
	private VendedoresClient vendedoresClient;
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("${json.properties.item.seller_id}")
	private String sellerItem;
	
	@Value("${json.properties.product_catalog.seller_id}")
	private String sellerItemProduct;
}
