package com.compare.products.characteristics.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.compare.products.characteristics.models.Publication;

public interface ComparationServices {
	
	@Cacheable(value = "ComparativeCharactsCache")
	public Object compare(List<Publication> publications);
}
