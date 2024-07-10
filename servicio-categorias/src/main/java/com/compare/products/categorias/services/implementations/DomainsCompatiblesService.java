package com.compare.products.categorias.services.implementations;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compare.products.categorias.clients.CategoriesClient;
import com.compare.products.categorias.models.DetailsCategory;
import com.compare.products.categorias.services.CategoryService;
import com.fasterxml.jackson.databind.JsonNode;

import feign.FeignException;
import lombok.Data;

@Service
@Scope("prototype")
@Data
@RefreshScope
public class DomainsCompatiblesService implements CategoryService {

	@Override
	public JsonNode findCategorybyId(String id) {
	    try {
	    	ResponseEntity<JsonNode>response= client.infoCategories(id);
	    	return response.getBody();
	    } catch (FeignException e) {
	    }
	    return null; 
	}

	@Override
	public DetailsCategory getDetailsByCategory(String id) {
		return null;
	}
	
	@Override
	public boolean areCompatibles(String[] ids) throws Exception {
		domains=new AtomicReference<Set<String>>(new HashSet<>());
		status=HttpStatus.OK;
		try(var scope=new StructuredTaskScope<>()){
			for (String id : ids) {
				scope.fork(() ->{
					JsonNode node=findCategorybyId(id);
					if(node==null) {
						status=HttpStatus.BAD_REQUEST;
					}
					domains.get().add(node.at(domain_name).asText().substring(3));
					return null;
				});
				
			}
			scope.join();
		}
		return domains.get().size()==1;
	}
	
	
	
	@Value("${compare.products.paths.info-category}")
	private String infoCategory;

	@Value("${json.properties.categories.domains_name}")
	private String domain_name;

	private AtomicReference<Set<String>> domains;
	
	private HttpStatus status;
	
	@Autowired
	private CategoriesClient client;
	

}
