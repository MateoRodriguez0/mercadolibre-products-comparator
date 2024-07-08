package com.products.compare.url.analisis.util;

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
	 public boolean resourceFound(String url,String regex) {
		return url.matches(regex);
	 } 
}
