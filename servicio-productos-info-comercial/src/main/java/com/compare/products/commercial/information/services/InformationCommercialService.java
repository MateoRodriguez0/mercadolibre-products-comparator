package com.compare.products.commercial.information.services;

import com.compare.products.commercial.information.models.CommercialInformation;
import com.compare.products.commercial.information.models.Shipping;
import com.compare.products.commercial.information.models.Warranty;
import com.fasterxml.jackson.databind.JsonNode;

public interface InformationCommercialService {

	public CommercialInformation getInfoCommercial(JsonNode jsonNode);
	public int getDiscount(JsonNode jsonNode);
	public double getRatingAverage(String itemId);
	public Warranty getWarranty(JsonNode jsonNode);
	public Shipping getShipping(JsonNode jsonNode);
	
}
