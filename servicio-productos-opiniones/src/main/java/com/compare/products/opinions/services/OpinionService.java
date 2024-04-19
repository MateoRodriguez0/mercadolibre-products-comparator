package com.compare.products.opinions.services;

import java.util.List;

import com.compare.products.opinions.models.Opinion;


public interface OpinionService {

	public Double getRatingAverage(String id) ;
	public List<Opinion> getReviews(String id, String token);
	
}
