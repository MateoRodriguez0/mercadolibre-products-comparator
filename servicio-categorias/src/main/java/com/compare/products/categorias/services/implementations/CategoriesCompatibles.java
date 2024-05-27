package com.compare.products.categorias.services.implementations;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compare.products.categorias.clients.CategoriesClient;
import com.compare.products.categorias.models.DetailsCategory;
import com.compare.products.categorias.services.CategoryService;
import com.fasterxml.jackson.databind.JsonNode;


@Service
@Scope("prototype")
public class CategoriesCompatibles implements CategoryService {

	@Override
	public JsonNode findCategorybyId(String id) {
		ResponseEntity<JsonNode>response= client.infoCategories(id);
    	return response.getBody();
	}

	@Override
	public DetailsCategory getDetailsByCategory(String id) {
		
		return null;
	}

	@Override
	public boolean areCompatibles(String[] ids) throws Exception {
		categoriesAtomic=new AtomicReference<Set<String>>(new HashSet<>());
		try(var scope= new StructuredTaskScope<>()){
			for (String id : ids) {
				scope.fork(() -> {
					categoriesAtomic.get().add(
					findCategorybyId(id).at("/path_from_root").get(0).at("/id").asText());
					return null;
				});
			}
			scope.join();
		}
		return categoriesAtomic.get().size()==1;
	}
	
	@Autowired
	private CategoriesClient client;

	private AtomicReference<Set<String>>categoriesAtomic;
}
