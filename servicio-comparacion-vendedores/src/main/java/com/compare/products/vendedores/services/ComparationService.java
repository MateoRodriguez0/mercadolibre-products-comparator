package com.compare.products.vendedores.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.compare.products.vendedores.models.ComarativeInfoSellers;
import com.compare.products.vendedores.models.SellerCompare;


public interface ComparationService {

	@Cacheable(value = "comparativeSellersCache")
	public ComarativeInfoSellers compare(List<SellerCompare> sellers)throws InterruptedException;
}
