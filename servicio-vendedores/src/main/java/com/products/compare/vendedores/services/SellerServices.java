package com.products.compare.vendedores.services;

import com.products.compare.vendedores.models.Seller;

public interface SellerServices {
	
	public Seller getSellerById(String id,String itemId);
}
