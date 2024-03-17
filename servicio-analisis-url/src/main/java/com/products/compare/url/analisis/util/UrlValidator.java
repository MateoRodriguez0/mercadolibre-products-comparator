package com.products.compare.url.analisis.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Clase de validacion para verificar que la url este bien escrita.
 * 
 * @Author Mateo Rodrigez c.
 * 20 feb. 2024 12:11:22 p. m.
 */
public class UrlValidator{

	 /**
	  * Crea un cliente http para hacer test a la url y validar 
	  * que la pagina este deisponible, utiliza el metodo valdiates para estar seguro que la url tenga una estructura valida para no crear un cliente http innecesario
	  * @param url Es la url que sera consumida por un RestTemplate
	  * @return Devuelve true si la pagina existe o false si no;
	  */
	 public static boolean resourceFound(String url) {
		 Pattern pattern = Pattern.compile("^https://www.mercadolibre.(com|com.([a-zA-Z]+))/(.+)[/]p/M([A-Z]{2}+[0-9]+)#[a-zA-Z0-9-_=]+&.*");
		 Pattern pattern2 =Pattern.compile("^https://articulo.mercadolibre.(com|com.([a-zA-Z]+))/M([A-Z]{2}+-[0-9]+-.*|[A-Z]{2}+-[0-9]+-(.*)#.*)");
		 Matcher matcher1 = pattern.matcher(url);
		 Matcher matcher2 = pattern2.matcher(url);

		 if (matcher1.matches()| matcher2.matches()) {
		   return true;
		 }

		 return false;


	 }
	 

	 
	 
}
