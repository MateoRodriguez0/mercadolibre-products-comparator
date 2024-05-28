package com.compare.products.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compare.products.clients.AnalisisUrlClient;
import com.compare.products.clients.CategoriasClient;
import com.compare.products.models.ItemDetails;

@Service
@Scope("prototype")
public class ComparePublication {

	public Object comparisonAttempt(String [] urls) {
		List<ItemDetails> itemDetails= new ArrayList<>();
		try(var scope= new StructuredTaskScope<>()){
			for (String url : urls) {
				scope.fork(() -> {
					ResponseEntity<?> details=urlClient.getCodeForUrl(url);
					if(details.getStatusCode().value()==200) {
						ItemDetails itemDetail= (ItemDetails)details.getBody();
						itemDetails.add(itemDetail);
					}
					return true;
				});
				
			}
			scope.join();
			if(itemDetails.size()== urls.length) {
				for (ItemDetails itemDetails2 : itemDetails) {
					System.out.println(itemDetails2);
				}

				return "correct";
			}
		
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "ERROR_FOUND";
		}
		
		
		
		return "ERROR_FOUND";
	}
	
	
	@Autowired
	private ComparePublicationsService service;
	@Autowired
	private AnalisisUrlClient urlClient;
	@Autowired
	private CategoriasClient categoriasClient;
}
