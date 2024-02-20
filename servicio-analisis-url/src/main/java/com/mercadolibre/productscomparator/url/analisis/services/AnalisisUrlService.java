package com.mercadolibre.productscomparator.url.analisis.services;

import com.mercadolibre.productscomparator.url.analisis.models.ItemDetails;

public interface AnalisisUrlService {

	/**
	 * Valida si la utl contiene el id del item.
	 * 
	 * @param url Es la direccion del producto en MercadoLibre
	 * @return Devuelve true si el id es de item o false si es de catalogo de producto.
	 */
	public boolean ContainIdItem(String url);
	
	/**
	 * Utiliza los metodos ContainIdItem y ContainIdProductCatalogo 
	 * para validar cual tipo de id se debe establecer en el objeto ItemDetails.
	 * 
	 * @param url Es la direccion del producto en MercadoLibre
	 * @return Devuelve el ItemDetails constuido con los id encontrados en la url.
	 */
	public ItemDetails CreateDetailsUrl(String url);
}
