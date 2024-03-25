package com.compare.products.categoria.analisis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DetailsCategory {

	private boolean in_products;
	private boolean in_services;
	private boolean in_vehicles;
	
	
}
