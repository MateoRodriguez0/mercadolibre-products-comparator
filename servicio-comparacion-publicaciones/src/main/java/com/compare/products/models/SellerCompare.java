package com.compare.products.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SellerCompare {
	private String publication_id;
	private String id;
	private String [] items;
}
