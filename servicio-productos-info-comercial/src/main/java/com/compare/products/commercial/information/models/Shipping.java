package com.compare.products.commercial.information.models;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipping implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ShippingMode mode;
    private String currency;
    private List<ShippingCost> handling_costs;
    
}