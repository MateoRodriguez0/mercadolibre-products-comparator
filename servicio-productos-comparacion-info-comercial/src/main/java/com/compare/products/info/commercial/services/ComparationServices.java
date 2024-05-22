package com.compare.products.info.commercial.services;

import java.util.List;

import com.compare.products.commons.models.Publication;
import com.compare.products.info.commercial.models.ComparativeInformationCommercial;

public interface ComparationServices {
	
	public ComparativeInformationCommercial compare(List<Publication> publications);
}
