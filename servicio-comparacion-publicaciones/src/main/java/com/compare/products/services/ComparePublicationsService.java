package com.compare.products.services;

import java.util.List;

import com.compare.products.models.ItemDetails;
import com.compare.products.models.PublicationComparative;

public interface ComparePublicationsService {
	public PublicationComparative getComparative(List<ItemDetails> details);
	public List<ItemDetails> getDetails(String [] urls);

}
