package com.products.compare.vendedores.services;

import com.products.compare.vendedores.models.ResponseTimeItem;

public interface ResponseTimeServices {

	public ResponseTimeItem timeOfResponseBySeller(String itemId);
	
	public String timeOfResponseBySeller(String [] itemsId);
}
