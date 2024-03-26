package com.compare.products.categoria.analisis.services.implementations;

import java.net.URI;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.compare.products.categoria.analisis.models.DetailsCategory;
import com.compare.products.categoria.analisis.services.CategoryService;
import com.compare.products.categoria.analisis.services.NameCategoryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
@Primary
public class CategoryServicesImpl implements CategoryService {

	@Override
	public JsonNode findCategorybyId(String id) {
		
		 ResponseEntity<ObjectNode>response= clientHttp
				 .getForEntity(URI.create(infoCategory.replace("{id}",id)),
						 ObjectNode.class);
		
		return response.getBody().get(FatherCategoriesProperity);
	}

	
	@Override
	public DetailsCategory getDetailsByCategory(String id) {
		try(var scope= new  StructuredTaskScope.ShutdownOnSuccess<>()){
			JsonNode fatherCategories= findCategorybyId(id);
			
			Subtask<Boolean> isProduct=scope.fork(() -> nameCategoryService
					.isProduct(fatherCategories));
			
			Subtask<Boolean> isService=scope.fork(() -> nameCategoryService
					.isService(fatherCategories));
			
			Subtask<Boolean> isVehicle=scope.fork(() -> nameCategoryService
					.isVehicle(fatherCategories));
			
			Subtask<Boolean> isProperty=scope.fork(() -> nameCategoryService
					.isProperty(fatherCategories));
			scope.join();
			
			if(isProduct.state()==State.SUCCESS) {
				return DetailsCategory.builder().in_products(true).build();
			}
			if(isProperty.state()==State.SUCCESS) {
				return DetailsCategory.builder().in_properties(true).build();
			}
			else if(isService.state()==State.SUCCESS) {
				return DetailsCategory.builder().in_services(true).build();
			}
			else if(isVehicle.state()==State.SUCCESS) {
				return DetailsCategory.builder().in_vehicles(true).build();
			}
			
		} catch (InterruptedException e) {
			throw new RuntimeException("No se pudo encontara a que categoria pertenece el id");
		}
		
		return null;
		
	}
	
	
	
	@Value("${compare.products.paths.info-category}")
	String infoCategory;
	
	@Value("${responses.json-property.father-category}")
	String FatherCategoriesProperity;
	
	@Autowired
	NameCategoryService nameCategoryService;
	
	@Autowired
	RestTemplate clientHttp;




}
