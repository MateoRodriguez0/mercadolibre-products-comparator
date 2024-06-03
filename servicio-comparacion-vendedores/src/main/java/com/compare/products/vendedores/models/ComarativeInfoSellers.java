package com.compare.products.vendedores.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ComarativeInfoSellers implements Serializable{

	
	public ComarativeInfoSellers() {
		this.links= new ArrayList<>();
		this.nickname= new ArrayList<>();
		this.positive_rating= new ArrayList<>();
		this.negative_rating= new ArrayList<>();
		this.neutral_rating= new ArrayList<>();
		this.mercadoLider_level= new ArrayList<>();
		this.sales= new ArrayList<>();
		this.experience= new ArrayList<>();
		this.location= new ArrayList<>();
		this.metrics= new MetricSellerComparative();
		this.response_time= new ArrayList<>();
		
	}
	private static final long serialVersionUID = 1L;
	private List<InfoSeller> nickname;
	private List<InfoSeller> links;
	private List<InfoSeller> sales;
	private List<InfoSeller> experience;
	private List<InfoSeller> mercadoLider_level;
	private List<InfoSeller> location;
	private List<InfoSeller> positive_rating;
	private List<InfoSeller> negative_rating;
	private List<InfoSeller> neutral_rating;
	private MetricSellerComparative metrics;
	private List<InfoSeller> response_time;
	
}
