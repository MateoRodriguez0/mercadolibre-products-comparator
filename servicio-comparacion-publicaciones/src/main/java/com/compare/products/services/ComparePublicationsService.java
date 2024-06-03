package com.compare.products.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

import com.compare.products.models.Publication;
import com.compare.products.models.PublicationComparative;
@EnableCaching
public interface ComparePublicationsService {
	
	@Cacheable(value = "comparativeProductCache")
	public PublicationComparative getComparative(
			List<Publication> publications)throws Exception;

}
