package com.compare.products.info.commercial.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ShippingComparative {
	public ShippingComparative() {
		this.products= new ArrayList<>();
	}
	public ShippingComparative(String city) {
		this.products= new ArrayList<>();
		this.city= city;
	}
	private String city;
	private List<HandlingComparative> products;
}
