package com.compare.products.commercial.information.models;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Shipping{
	private ShippingMode mode;
    private String currency;
    private List<ShippingCost> costs;
    
}