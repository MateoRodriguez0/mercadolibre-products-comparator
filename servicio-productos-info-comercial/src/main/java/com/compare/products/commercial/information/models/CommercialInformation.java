package com.compare.products.commercial.information.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommercialInformation {
	
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
