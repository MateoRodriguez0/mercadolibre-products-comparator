package com.compare.products.commercial.information.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.compare.products.commercial.information.models.CommercialInformation;
import com.compare.products.commercial.information.models.PublicationType;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class InformationCommercialFacade {

	
	public CommercialInformation getInfoCommercial(JsonNode jsonNode,
			PublicationType publicationType) {
		if(publicationType==PublicationType.item) {
			return context.getBean("informationItem", InformationCommercialService.class)
					.getInfoCommercial(jsonNode);
			}
		else {
			return context.getBean("informationProductCatalog", InformationCommercialService.class)
					.getInfoCommercial(jsonNode);
			}
		}
	
	@Autowired
	private ApplicationContext context;
}

