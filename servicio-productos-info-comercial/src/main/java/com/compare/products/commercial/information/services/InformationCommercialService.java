package com.compare.products.commercial.information.services;

import org.springframework.web.client.HttpClientErrorException;

import com.compare.products.commercial.information.models.CommercialInformation;
import com.compare.products.commercial.information.models.Shipping;
import com.compare.products.commercial.information.models.Warranty;
import com.fasterxml.jackson.databind.JsonNode;

public interface InformationCommercialService {

	public CommercialInformation getInfoCommercial(JsonNode jsonNode,String token) throws HttpClientErrorException ;
	public int getDiscount(JsonNode jsonNode);
	public double getRatingAverage(String itemId,String token);
	public Warranty getWarranty(JsonNode jsonNode);
	public Shipping getShipping(JsonNode jsonNode,String token);
	
}
