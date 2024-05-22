package com.products.compare.vendedores.services;

import com.compare.products.commons.models.Seller;

public interface SellerServices {
	
	public Seller getSellerById(String id,String []items);
}
