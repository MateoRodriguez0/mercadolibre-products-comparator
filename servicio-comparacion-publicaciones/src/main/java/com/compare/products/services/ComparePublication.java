package com.compare.products.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compare.products.clients.AnalisisUrlClient;
import com.compare.products.clients.MercadolibreFeignClient;
import com.compare.products.models.ItemDetails;
import com.compare.products.models.Publication;
import com.compare.products.models.PublicationType;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Scope("prototype")
public class ComparePublication {

	@Cacheable(value = "comparativeProductCache", key = "#urls",
			unless = "#result == 'NOT_IS_PRODUCT' || #result == 'INTERNAL_SERVER_ERROR'"
					+ "|| #result == 'NUMBER_LINKS_NOT_ACCEPTED' || #result == 'ALL_URLS_INVALID'"
					+ "|| #result == 'ONE_URL_INVALID'")
	public Object comparisonAttempt(String [] urls) {
		if(urls.length<2) {
			return "NUMBER_LINKS_NOT_ACCEPTED";
		}
		List<ItemDetails> valids= getTotalvalidUrls(urls);
		if(valids.size() ==0) {
			return "ALL_URLS_INVALID";
		}
		if(valids.size()<urls.length) {
			return "ONE_URL_INVALID";
		}
		if(valids.size()==urls.length) {
			List<Publication> publications= getInfoPublications(valids);
			boolean areProducts= categoryService.areProducts(publications.stream()
					.map(p ->p.getPublication()).toList());
			if (areProducts) {
				try {
					return service.getComparative(publications);
				} catch (Exception e) {
					e.printStackTrace();
					return "INTERNAL_SERVER_ERROR";
				}
			}
			else {
				return "NOT_IS_PRODUCT";
			}
		}
		return "INTERNAL_SERVER_ERROR";
	}
	
	
	
	public List<Publication> getInfoPublications(List<ItemDetails> ids) {
		List<Publication> publications= new ArrayList<>();
		List<JsonNode> infos= new ArrayList<>();
		String token= request.getHeader("Authorization");
		try(var scope= new StructuredTaskScope<>()){
			for (ItemDetails id : ids) {
					scope.fork(() -> {
						if(id.getCatalog_product_id()!=null) {
							ResponseEntity<JsonNode> info=apiClient
									.getProduct(id.getCatalog_product_id(), token);
							Publication publication=new Publication(
									info.getBody(),PublicationType.catalog_product.name());
					
							publications.add(publication);
							infos.add(info.getBody());
							return true;
						}
						ResponseEntity<JsonNode> info=apiClient.getItem(id.getId(), token);
						Publication publication= new Publication(
								info.getBody(),PublicationType.item.name());
							
						publications.add(publication);
						infos.add(info.getBody());
						return true;
					});
			}
			scope.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		return publications;
	}
	
	public List<ItemDetails> getTotalvalidUrls(String [] urls) {
		List<ItemDetails> itemDetails= new ArrayList<>();
		try(var scope= new StructuredTaskScope<>()){
			for (String url : urls) {
				scope.fork(() -> {
					ResponseEntity<ItemDetails> details=urlClient.getCodeForUrl(url);
					if(details.getStatusCode().value()== 200) {
						ItemDetails itemDetail=details.getBody();
						itemDetails.add(itemDetail);
					}
					return true;
				});
			}
			scope.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		return itemDetails;
		
	}
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private MercadolibreFeignClient apiClient;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ComparePublicationsService service;
	@Autowired
	private AnalisisUrlClient urlClient;
}
