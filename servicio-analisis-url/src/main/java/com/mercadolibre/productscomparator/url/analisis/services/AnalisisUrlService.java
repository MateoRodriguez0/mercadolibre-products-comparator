package com.mercadolibre.productscomparator.url.analisis.services;

import com.mercadolibre.productscomparator.url.analisis.models.UrlDetails;

public interface AnalisisUrlService {

	public boolean ContainIdProductCatalogo(String url);
	
	public boolean ContainIdItem(String url);
	
	public UrlDetails CreateDetailsUrl(String url);
}
