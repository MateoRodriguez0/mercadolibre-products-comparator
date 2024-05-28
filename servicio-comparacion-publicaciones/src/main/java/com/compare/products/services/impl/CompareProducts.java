package com.compare.products.services.impl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.models.ItemDetails;
import com.compare.products.models.PublicationComparative;
import com.compare.products.services.ComparePublicationsService;

@Service
@Primary
@Scope("prototype")
public class CompareProducts implements ComparePublicationsService {

	@Override
	public PublicationComparative getComparative(List<ItemDetails> details) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemDetails> getDetails(String[] urls) {
		// TODO Auto-generated method stub
		return null;
	}
}
