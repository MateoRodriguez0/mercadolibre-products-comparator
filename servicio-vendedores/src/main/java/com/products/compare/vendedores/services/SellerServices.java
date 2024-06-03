package com.products.compare.vendedores.services;

import org.springframework.cache.annotation.Cacheable;

import com.products.compare.vendedores.models.Seller;

public interface SellerServices {
	
	@Cacheable(value = "InfoSellerPublicationCache")
	public Seller getSellerById(String id,String []items);
}
