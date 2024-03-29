package com.compare.products.commercial.information.services;

import com.compare.products.commercial.information.models.CommercialInformation;
import com.compare.products.commercial.information.models.PublicationType;
import com.fasterxml.jackson.databind.JsonNode;

public interface InformationCommercialService {

	public CommercialInformation getInfoCommercial(JsonNode jsonNode,String token,PublicationType publicationType);
	public CommercialInformation infoCommercialForItemType(JsonNode jsonNode,String token);
	public CommercialInformation infoCommercialForProductType(JsonNode jsonNode,String token);
	public int getDiscount(JsonNode jsonNode,PublicationType publicationType);
	
	
}
