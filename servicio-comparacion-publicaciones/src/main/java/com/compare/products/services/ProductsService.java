package com.compare.products.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.clients.MercadolibreFeignClient;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Scope("prototype")
@RefreshScope
public class ProductsService {

	public List<String> getItemsId(JsonNode info,String token) throws InterruptedException{
		if (info.has("type")) {
			if(info.at(childrensId).size()!=0) {
				return getItemsofChildrens(info.at(childrensId), token);
			}
			if(!info.at(parentId).asText().equals("null")) {
				JsonNode product= apiClient.getProduct(info.at(parentId).asText(), token).getBody();
				return getItemsofChildrens(product.at(childrensId), token);
			}
			else {
				return List.of(info.at(productItemId).asText());
			}
		}
		return List.of(info.at(itemId).asText());
	}
	
	
	private List<String> getItemsofChildrens(JsonNode Childrens,String token) throws InterruptedException{
		List<String> ids= new ArrayList<>();
		List<Subtask<String>> subtasks= new ArrayList<>();
		
		try (var scope= new StructuredTaskScope<String>()){
			for (JsonNode id : Childrens) {
				subtasks.add(scope.fork(() -> {
					try {
	                    JsonNode product = apiClient.getProduct(id.asText(), token).getBody();
	                    return product.at(this.productItemId).asText();
	                } catch (Exception e) {
	                    return "";
	                }
				}));	
			}
			scope.join();
			for (Subtask<String> subtask : subtasks) {
				if(subtask.get().length()!=0) {
					ids.add(subtask.get());
				}
			}
		}
		return ids;
	}
	
	@Autowired
	private MercadolibreFeignClient apiClient;
	
	@Value("${json.properties.product_catalog.item_id}")
	private String productItemId;
	
	@Value("${json.properties.item.id}")
	private String itemId;
	
	@Value("${json.properties.product_catalog.parent}")
	private String parentId;
	
	@Value("${json.properties.product_catalog.children_ids}")
	private String childrensId;
}
