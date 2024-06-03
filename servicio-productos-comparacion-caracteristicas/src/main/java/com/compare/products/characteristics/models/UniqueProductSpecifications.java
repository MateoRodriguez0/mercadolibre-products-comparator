package com.compare.products.characteristics.models;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UniqueProductSpecifications implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private List<String> Specifications;
	
}
