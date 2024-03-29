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
	private String currency_id;
	private boolean available;
	private int discount;
	private double rating_average;
	private Shipping shipping;
	private Warranty warranty;
	private String international_delivery_mode;
	private String payment_methods;

	
	
	@Data
	private class Warranty{
		private double number;
        private String unit;
	}
	
	
	@Data
	private class Shipping{
		private ShippingMode mode;
        private double cost;
	}
	
	
	
	
}
