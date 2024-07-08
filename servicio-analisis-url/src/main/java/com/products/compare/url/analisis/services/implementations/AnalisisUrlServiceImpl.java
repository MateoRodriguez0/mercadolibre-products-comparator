package com.products.compare.url.analisis.services.implementations;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.products.compare.url.analisis.models.ItemDetails;
import com.products.compare.url.analisis.services.AnalisisUrlService;
import com.products.compare.url.analisis.services.PaisService;
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
@RefreshScope
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
					.supplyAsync(() -> new UrlValidator().resourceFound(url,regex),service);
			
			CompletableFuture<List<String>> paises=CompletableFuture
					.supplyAsync(this::getPaises,service);
			
			CompletableFuture<ItemDetails> taskItemDetails=CompletableFuture
					.supplyAsync(() ->{
						try {
							String code = serchCodeForUrl(url,paises.get());
							
							if(code.contains("-")) {
								return ItemDetails.builder()
										.id(code.replace("-",""))
										.build();
							}
							else {
								return ItemDetails.builder()
										.catalog_product_id(code.replace("#", ""))
										.build();
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
		 Pattern patternPro = Pattern.compile("(p\\/"+site+"[0-9]+)");
		 Matcher matcherPro = patternPro.matcher(url);
		 if(matcherPro.find()) {
			   return matcherPro.group().replace("p/", "");
			 
		 }
		 throw new RuntimeException("No se encontro ningun codigo de pais en la url");
		 
	}
	
	/**
	 * Utiliza el client para realizar una peticion al recurso de paises.
	 * 
	 * @return devuelve una lista que contiene los codigos de los paises.
	 */
	private List<String> getPaises(){
		return paisService.getPaises();
	}
	
	
	@Autowired
	private PaisService paisService;
	
	@Value("${compare.products.regex-url}")
	private String regex;	 
}
