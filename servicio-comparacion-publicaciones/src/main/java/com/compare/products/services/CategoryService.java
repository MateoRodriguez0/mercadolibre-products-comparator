package com.compare.products.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compare.products.clients.CategoriasClient;
import com.compare.products.models.DetailsCategory;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Service
@Scope("prototype")
@RefreshScope
public class CategoryService {

	private String [] getCategoriesFormPublications(List<JsonNode> info) {
		return info.stream().map(j ->{
			if(j.at(productCategoryId).asText().length()!=0) {
				return j.at(productCategoryId).asText();
			}
			return j.at(itemCategoryId).asText();
		}).toArray(String[]::new);
	}
	
	public boolean areProducts(List<JsonNode> infos) {
		String [] categories=getCategoriesFormPublications(infos);
		List<DetailsCategory> details= new ArrayList<>();
		try(var scope= new StructuredTaskScope<>()){
			for (String cat : categories) {
				scope.fork(() -> {
					ResponseEntity<DetailsCategory> response=
							categoriasClient.getTypeOfPublicationByProductCategory(cat);
					if(response.getStatusCode().value()==200) {
						DetailsCategory catDetail= response.getBody();
						details.add(catDetail);
					}
					return true;
				});
				
			}
			scope.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		return details.stream()
				.filter(d ->d.isIn_products())
				.toList()
				.size()==categories.length;
	}
	
	
	@Autowired
	private CategoriasClient categoriasClient;
	
	@Value("${json.properties.item.category_id}")
	private String itemCategoryId;
	@Value("${json.properties.product_catalog.category_id}")
	private String productCategoryId;
}
