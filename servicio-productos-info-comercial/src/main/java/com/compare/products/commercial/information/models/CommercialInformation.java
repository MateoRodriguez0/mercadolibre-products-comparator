package com.compare.products.commercial.information.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommercialInformation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String brand;
	private double price;
	private int discount_porcentage;
	private String currency_id;
	private String total_sales;
	private String availables;
	private double rating_average;
	private String international_delivery_mode;
	private String payment_methods;
	private Warranty warranty;
	private Shipping shipping;
	
	
}
