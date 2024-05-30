package com.compare.products.services;

import java.util.List;

import com.compare.products.models.ItemDetails;
import com.compare.products.models.PublicationComparative;
import com.fasterxml.jackson.databind.JsonNode;


public interface ComparePublicationsService {
	public PublicationComparative getComparative(List<JsonNode> nodess);
	public List<ItemDetails> getDetails(String [] urls);

}
