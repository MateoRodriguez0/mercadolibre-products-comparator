package com.compare.products.categorias.models;

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
	private boolean in_properties;
	
}
