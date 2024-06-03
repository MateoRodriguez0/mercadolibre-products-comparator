package com.compare.products.vendedores.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MetricSellerComparative implements Serializable{

	
	public MetricSellerComparative() {
		this.cancellations= new ArrayList<>();
		this.claims= new ArrayList<>();
		this.delayed_handling_time= new ArrayList<>();

	}
	private static final long serialVersionUID = 1L;
	private List<InfoSeller> claims;
	private List<InfoSeller> cancellations;
	private List<InfoSeller> delayed_handling_time;
}
