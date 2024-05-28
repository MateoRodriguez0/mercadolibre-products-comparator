package com.compare.products.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Clase de modelo para representar los detalles de un item, 
 * como lo son id del item e id de producto de cataloqo.
 * 
 * @Author Mateo Rodrigez c.
 * 20 feb. 2024 11:43:15 a. m.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetails {

	private String id;
	private String catalog_product_id;
	
}
