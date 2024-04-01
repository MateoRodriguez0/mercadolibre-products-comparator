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
	
}
