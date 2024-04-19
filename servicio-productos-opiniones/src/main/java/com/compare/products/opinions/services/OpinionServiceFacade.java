package com.compare.products.opinions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.compare.products.opinions.models.PublicationType;

@Service
public class OpinionServiceFacade {
	
	public OpinionService getOpinionService(PublicationType publicationType) {
		if(publicationType==PublicationType.item) {
			return context.getBean("opinionItem", OpinionService.class);
			}
		else {
			return context.getBean("opinionProductCatalog", OpinionService.class);
			}
	}
	
	@Autowired
	private ApplicationContext context;
}
