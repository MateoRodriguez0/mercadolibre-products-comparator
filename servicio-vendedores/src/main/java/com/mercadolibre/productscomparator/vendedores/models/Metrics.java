package com.mercadolibre.productscomparator.vendedores.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metrics {
	private String period;
	private int sales;
	private double cancellations;
	private double claims;
	private double delayed_handling_time;
	
}
