package com.compare.products.commercial.information.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.commercial.information.models.CommercialInformation;
import com.compare.products.commercial.information.models.PublicationType;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Scope("prototype")
public class InformationCommercialFacade {

	
	public CommercialInformation getInfoCommercial(JsonNode jsonNode,
			PublicationType publicationType) {
		if(publicationType==PublicationType.item) {
			return context.getBean("informationItem", InformationCommercialService.class)
					.getInfoCommercial(jsonNode,request.getHeader("Authorization"));
			}
		else {
			return context.getBean("informationProductCatalog", InformationCommercialService.class)
					.getInfoCommercial(jsonNode,request.getHeader("Authorization"));
			}
		}
	
	@Autowired
	private ApplicationContext context;
	
	
	@Autowired
	private HttpServletRequest request;
}

