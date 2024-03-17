package com.products.compare.url.analisis.services.implementations;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.products.compare.url.analisis.clients.MercadoLibreSitesClient;
import com.products.compare.url.analisis.models.ItemDetails;
import com.products.compare.url.analisis.services.AnalisisUrlService;
import com.products.compare.url.analisis.util.UrlValidator;
/**
 * Clase de servicio para busqueda de codigo de item o 
 * deproducto de catalogo en la url de una publicacion en mercadolibre.
 * 
 * @Author Mateo Rodrigez c.
 * 18 feb. 2024 11:48:16 a. m.
 */
@Service
@Primary
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
		try {
			ExecutorService service= Executors.newVirtualThreadPerTaskExecutor();
			CompletableFuture<Boolean> valida=CompletableFuture
					.supplyAsync(() -> UrlValidator.resourceFound(url),service);
			
			CompletableFuture<List<String>> paises=CompletableFuture
					.supplyAsync(this::getPaises,service);
			
			CompletableFuture<ItemDetails> taskItemDetails=CompletableFuture
					.supplyAsync(() ->{
						try {
							String code = serchCodeForUrl(url,paises.get());
							if(code.contains("-")) {
								return new ItemDetails(code.replace("-",""),null);
							}
							else {
								return new ItemDetails(null, code.replace("#", ""));
							}
						} catch (InterruptedException | ExecutionException e) {
							return null;
						}}
					,service);
			while(valida.isDone()|| !paises.isDone()) {
				if(valida.get()){
					return taskItemDetails.get();
				}
				else {
					paises.cancel(true);
					taskItemDetails.cancel(true);
					service.shutdown();
					break;
					
				}	
			}
			
			} catch (InterruptedException | ExecutionException e) {
				
			}
		return null;
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
		 
		 Pattern patternItem = Pattern.compile("("+site+"\\-[0-9]+)-");
		 Matcher matcherItem = patternItem.matcher(url);

		 if (matcherItem.find()) {
			   return matcherItem.group();
			 }
		 Pattern patternPro = Pattern.compile("("+site+"[0-9]+)#");
		 Matcher matcherPro = patternPro.matcher(url);
		 if(matcherPro.find()) {
			   return matcherPro.group();
			 
		 }
		 throw new RuntimeException("No se encontro ningun codigo de pais en la url");
		 
	}
	
	/**
	 * Utiliza el client para realizar una peticion al recurso de paises.
	 * 
	 * @return devuelve una lista que contiene los codigos de los paises.
	 */
	private List<String> getPaises(){
		return client.getPaises()
				.parallelStream()
				.map(j ->j.get("id").asText())
				.collect(Collectors.toList());
	}
	
	
	 @Autowired
	 private MercadoLibreSitesClient client;


	 
	 
}
