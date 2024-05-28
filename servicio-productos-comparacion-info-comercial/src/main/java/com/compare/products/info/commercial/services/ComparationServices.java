package com.compare.products.info.commercial.services;

import java.util.List;

import com.compare.products.info.commercial.models.ComparativeInformationCommercial;
import com.compare.products.info.commercial.models.Publication;

public interface ComparationServices {
	
	public ComparativeInformationCommercial compare(List<Publication> publications);
}
