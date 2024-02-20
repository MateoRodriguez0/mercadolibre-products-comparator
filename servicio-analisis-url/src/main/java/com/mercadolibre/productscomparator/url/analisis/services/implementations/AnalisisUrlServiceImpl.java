package com.mercadolibre.productscomparator.url.analisis.services.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolibre.productscomparator.url.analisis.clients.MercadoLibreSitesClient;
import com.mercadolibre.productscomparator.url.analisis.models.ItemDetails;
import com.mercadolibre.productscomparator.url.analisis.services.AnalisisUrlService;
/**
 * Clase de servicio para busqueda de codigo de item o 
 * deproducto de catalogo en la url de una publicacion en mercadolibre.
 * 
 * @Author Mateo Rodrigez c.
 * 18 feb. 2024 11:48:16 a. m.
 */
@Service
public class AnalisisUrlServiceImpl implements AnalisisUrlService {

	@Override
	public boolean ContainIdItem(String code) {
		if(code.contains("-")) {
			return true;
		}
		return false;
	}

	
	@Override
	public ItemDetails CreateDetailsUrl(String url) {
		String code=serchCodeForUrl(url);
		ItemDetails details= ItemDetails
				.builder()
				.id(code)
				.catalog_product_id(code)
				.build();
		
		if(ContainIdItem(code)) {
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
			// Validar que el codigo sea de un item 
			if(initcode==0&& url.contains(site+"-")) {
				initcode=url.indexOf(site);
				codigo.append(site).append("-");
			}
			// Validar que el codigo sea de un prodcuto de catalogo 
			if(initcode==0&& url.contains("p/"+site)){
				initcode=url.indexOf("p/"+site)+2;
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
	
	/**
	 * Utiliza el client para realizar una peticion al recurso de paises.
	 * 
	 * @return devuelve una lista que contiene los codigos de los paises.
	 */
	private List<String> getPaises(){
		return client.getPaises()
				.stream()
				.map(j ->j.get("id").asText())
				.collect(Collectors.toList());
	}
	
	
	 @Autowired
	 private MercadoLibreSitesClient client;


	 
	 
}
