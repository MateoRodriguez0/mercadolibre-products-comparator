package com.compare.products.categorias.services;

import com.fasterxml.jackson.databind.JsonNode;


public interface NameCategoryService {

	
	public Boolean isProduct (JsonNode Categories) throws Exception;
	
	public Boolean isService (JsonNode Categories) throws Exception;
	
	public Boolean isVehicle (JsonNode Categories) throws Exception;
	
	public Boolean isProperty (JsonNode Categories) throws Exception;
	
}
