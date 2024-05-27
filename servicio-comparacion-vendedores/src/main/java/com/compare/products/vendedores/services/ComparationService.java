package com.compare.products.vendedores.services;

import java.util.List;

import com.compare.products.vendedores.models.ComarativeInfoSellers;
import com.compare.products.vendedores.models.SellerCompare;

public interface ComparationService {

	public ComarativeInfoSellers compare(List<SellerCompare> sellers)throws InterruptedException;
}
