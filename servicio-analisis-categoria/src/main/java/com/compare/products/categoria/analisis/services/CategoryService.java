package com.compare.products.categoria.analisis.services;

import com.compare.products.categoria.analisis.models.DetailsCategory;
import com.fasterxml.jackson.databind.JsonNode;


public interface CategoryService {

	public JsonNode findCategorybyId(String id);
	
	public DetailsCategory getDetailsByCategory(String id);
	
}
