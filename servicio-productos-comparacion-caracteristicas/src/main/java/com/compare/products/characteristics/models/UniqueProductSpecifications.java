package com.compare.products.characteristics.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UniqueProductSpecifications {
	private String id;
	private List<String> Specifications;
	
}
