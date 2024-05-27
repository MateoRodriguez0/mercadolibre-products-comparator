package com.compare.products.categorias.services;

import com.compare.products.categorias.models.DetailsCategory;
import com.fasterxml.jackson.databind.JsonNode;


public interface CategoryService {

	public JsonNode findCategorybyId(String id);
	
	public DetailsCategory getDetailsByCategory(String id);
	
	public boolean areCompatibles(String [] ids)throws Exception;
	
}
