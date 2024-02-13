package com.mercadolibre.productscomparator.url.analisis.services.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolibre.productscomparator.url.analisis.clients.MercadoLibreSitesClient;
import com.mercadolibre.productscomparator.url.analisis.models.UrlDetails;
import com.mercadolibre.productscomparator.url.analisis.services.AnalisisUrlService;

@Service
public class AnalisisUrlServiceImpl implements AnalisisUrlService {

	@Override
	public boolean ContainIdProductCatalogo(String url) {
		String code=serchCodeForUrl(url);
		if(!code.contains("-")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean ContainIdItem(String url) {
		String code=serchCodeForUrl(url);
		if(code.contains("-")) {
			return true;
		}
		return false;
	}

	
	@Override
	public UrlDetails CreateDetailsUrl(String url) {
		String code=serchCodeForUrl(url);
		UrlDetails details= UrlDetails
				.builder()
				.id(code)
				.catalog_product_id(code)
				.build();
		
		if(ContainIdItem(url)) {
			details.setId(code.replace("-", ""));
			details.setCatalog_product_id(null);
		}
		else {
			details.setId(null);
		}
		return details;
	}
	
	
	/**
	 * 
	 * @param url Es la url desicfrara el codigo del producto
	 * @return Devuelve el codigo del producto 
	 */
	public String serchCodeForUrl(String url){
		StringBuilder codigo=new StringBuilder();
		int initcode=0;
	

		for (String site : getPaises()) {
	
			if(initcode==0&& url.contains(site+"-")) {
				initcode=url.indexOf(site);
				codigo.append(site).append("-");
			}
			
			if(initcode==0&& url.contains(site)){
				initcode=url.indexOf(site);
				codigo.append(site);
			}
			
			
		}
		
		
		if(codigo.length()!=0) {
	
			for (char c : url.substring(initcode+codigo.length()).toCharArray()) {
				 
		        if (Character.isDigit(c)) {
		        	codigo.append(c);

		        } else {
		            break;
		        }
			}
		}
		return codigo.toString();
	}
	
	
	private List<String> getPaises(){
		
		return client.getPaises()
				.stream()
				.map(j ->j.get("id").asText())
				.collect(Collectors.toList());
	}
	
	
	 @Autowired
	 private MercadoLibreSitesClient client;
	 
	 
	 
}
