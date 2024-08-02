package com.compare.products.info.commercial.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComparativeInformationCommercial {
	
	public ComparativeInformationCommercial() {
		this.brand= new ArrayList<>();
		this.price= new ArrayList<>();
		this.discount_porcentage= new ArrayList<>();
		this.total_sales= new ArrayList<>();
		this.availables= new ArrayList<>();
		this.rating_average= new ArrayList<>();
		this.international_delivery_mode= new ArrayList<>();
		this.payment_methods= new ArrayList<>();
		this.warranty= new ArrayList<>();
		this.permalink= new ArrayList<>();
		this.picture= new ArrayList<>();
		this.name= new ArrayList<>();

	}
	
	private List<InfoCommercialProdcut> brand;
	private List<InfoCommercialProdcut> name;
	private List<InfoCommercialProdcut> permalink;
	private List<InfoCommercialProdcut> picture;
	private List<InfoCommercialProdcut> price;
	private List<InfoCommercialProdcut> discount_porcentage;
	private List<InfoCommercialProdcut> total_sales;
	private List<InfoCommercialProdcut> availables;
	private List<InfoCommercialProdcut> rating_average;
	private List<InfoCommercialProdcut> international_delivery_mode;
	private List<InfoCommercialProdcut> payment_methods;
	private List<InfoCommercialProdcut> warranty;
	private List<InfoCommercialProdcut> shipping;
	private List<ShippingComparative> shipping_comparative;
	
}
