package com.compare.products.commercial.information.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ShippingCost {

	private String address;
	private double cost;
	private String estimated_delivery_time;
	private String estimated_delivery_time_offset;
	

	
}
