package com.compare.products.categoria.analisis.services.implementations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.compare.products.categoria.analisis.services.NameCategoryService;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class NameCategoryServiceImpl implements NameCategoryService{

	@Override
	public Boolean isProduct(JsonNode Categories) throws Exception  {
		boolean isProduct=true;
		for (JsonNode jsonNode : Categories) {
			if(jsonNode.get("id").asText().endsWith(serviceEndCode) ||
					jsonNode.get("id").asText().endsWith(vehicleEndCode) ||
						jsonNode.get("id").asText().endsWith(propertyEndCode)) {
				isProduct=false;
			}
				
		}
		if(isProduct)
			return isProduct;
		
		throw new Exception("La categoria no pertenece a productos");
	}

	@Override
	public Boolean isService(JsonNode Categories) throws Exception {
		for (JsonNode jsonNode : Categories) {
			if(jsonNode.get("id").asText().endsWith(serviceEndCode)) {
				return true;
			}	
		}
		throw new Exception("La categoria no pertenece a servicio");
	}

	@Override
	public Boolean isVehicle(JsonNode Categories)throws Exception {
		for (JsonNode jsonNode : Categories) {
			if(jsonNode.get("id").asText().endsWith(vehicleEndCode)) {
				return true;
			}
		}
		throw new Exception("La categoria no pertenece a vehiculos");
	}
	
	
	@Override
	public Boolean isProperty(JsonNode Categories) throws Exception {
		for (JsonNode jsonNode : Categories) {
			if(jsonNode.get("id").asText().endsWith(propertyEndCode)) {
				return true;
			}
		}
		throw new Exception("La categoria no pertenece a inmuebles");
	}
	
	
	
	@Value("${categories.end-code.service}")
	private String serviceEndCode;
	
	@Value("${categories.end-code.vehicle}")
	private String vehicleEndCode;

	@Value("${categories.end-code.property}")
	private String propertyEndCode;


}
