package com.compare.products.info.commercial.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipping{
	private ShippingMode mode;
    private String currency;
    private List<ShippingCost> handling_costs;
    
}