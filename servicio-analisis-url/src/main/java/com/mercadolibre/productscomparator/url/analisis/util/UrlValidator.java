package com.mercadolibre.productscomparator.url.analisis.util;

import java.util.regex.Pattern;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;

import java.util.regex.Matcher;

/**
 * Clase de validacion para verificar que la url este bien escrita.
 * 
 * @Author Mateo Rodrigez c.
 * 20 feb. 2024 12:11:22 p. m.
 */
public class UrlValidator {

	 private static final String URL_REGEX =
	            "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
	            "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
	            "([).!';/?:,][[:blank:]])?$";
	 
	 private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
	 
	 /**
	  * Utiliza una expresion regular para Validar 
	  * que una url este bien escrita.
	  * 
	  * @param url Es la url que sera validada.
	  * @return Devuelve true si la url es valido o false si no lo es.
	  */
	 public static boolean validate(String url){
		 if (url == null) {
			 return false;
			 }
	     Matcher matcher = URL_PATTERN.matcher(url);
	     return matcher.matches();
	    }
	 
	 /**
	  * Crea un cliente http para hacer test a la url y validar 
	  * que la pagina este deisponible, utiliza el metodo valdiates para estar seguro que la url tenga una estructura valida para no crear un cliente http innecesario
	  * @param url Es la url que sera consumida por un RestTemplate
	  * @return Devuelve true si la pagina existe o false si no;
	  */
	 public static boolean resourceFound(String url) {
		if(validate(url)) {
			try {
				 new RestTemplateBuilder().build().getForEntity(url, String.class);
				 return true;
				} catch (HttpClientErrorException e) {	
				}
			}
		return false; 
		}
}
