package com.compare.products.categorias.services.implementations;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compare.products.categorias.clients.CategoriesClient;
import com.compare.products.categorias.models.DetailsCategory;
import com.compare.products.categorias.services.CategoryService;
import com.compare.products.categorias.services.NameCategoryService;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Primary
@Scope("prototype")
@RefreshScope
public class CategoryServicesImpl implements CategoryService {

	@Override
	public JsonNode findCategorybyId(String id) {
		ResponseEntity<JsonNode>response= client.infoCategories(id);
		return response.getBody().at(FatherCategoriesProperity);
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
	
	@Override
	public boolean areCompatibles(String[] ids) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Value("${compare.products.paths.info-category}")
	private String infoCategory;
	
	@Value("${responses.json-property.father-category}")
	private String FatherCategoriesProperity;
	
	@Autowired
	private NameCategoryService nameCategoryService;
	

	@Autowired
	private CategoriesClient client;





}
