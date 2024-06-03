package com.compare.products.categorias.services;

import org.springframework.cache.annotation.Cacheable;

import com.compare.products.categorias.models.DetailsCategory;
import com.fasterxml.jackson.databind.JsonNode;


public interface CategoryService {

	@Cacheable(value = "CategoriasCache")
	public JsonNode findCategorybyId(String id);
	
	@Cacheable(value = "CategoriasCache")
	public DetailsCategory getDetailsByCategory(String id);
	
	@Cacheable(value = "CategoriasCache")
	public boolean areCompatibles(String [] ids)throws Exception;
	
}
