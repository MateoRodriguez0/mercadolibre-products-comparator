package com.mercadolibre.productscomparator.url.analisis.services.implementations;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolibre.productscomparator.url.analisis.clients.MercadoLibreSitesClient;
import com.mercadolibre.productscomparator.url.analisis.models.ItemDetails;
import com.mercadolibre.productscomparator.url.analisis.services.AnalisisUrlService;
import com.mercadolibre.productscomparator.url.analisis.util.UrlValidator;
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
		Subtask<List<String>> paises=null;
		Subtask<Boolean> valida=null;
		
		try(var scope= new StructuredTaskScope<>()){
			paises=scope.fork(this::getPaises);
			valida=scope.fork(() -> UrlValidator.resourceFound(url));
		
			scope.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(!valida.get()) {
			return null;
		}
		
		String code=serchCodeForUrl(url,paises.get());
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
	public String serchCodeForUrl(String url,List<String>paises){
		String codigo=null;
		try(var scope= new StructuredTaskScope.ShutdownOnSuccess<String>()){
			paises.forEach(site ->{
				scope.fork(() -> searchCodeForPais(url, site));
			});
			scope.join();
			codigo= scope.result();
		} catch (InterruptedException | ExecutionException e) {}

		return codigo;
	

		
	}
	
	 private String searchCodeForPais(String url, String site) {
		 StringBuilder codigo=new StringBuilder();
		 int initcode=0;
		 if(url.contains(site+"-")|url.contains("p/"+site)) {
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
		 else {
			 throw new RuntimeException();
		 }
		 for (char c : url.substring(initcode+codigo.length()).toCharArray()) {
			 if (Character.isDigit(c)) {
				 codigo.append(c);
				 }
			 else {
				break;
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
