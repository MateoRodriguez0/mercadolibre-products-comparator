package com.compare.products.vendedores.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellerCompare {
	private String publication_id;
	private String id;
	private String [] items;
}
