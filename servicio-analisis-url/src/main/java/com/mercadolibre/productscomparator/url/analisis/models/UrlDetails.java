package com.mercadolibre.productscomparator.url.analisis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlDetails {

	private String id;
	private String catalog_product_id;
	
}
