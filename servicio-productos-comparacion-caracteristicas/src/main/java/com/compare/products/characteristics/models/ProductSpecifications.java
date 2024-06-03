package com.compare.products.characteristics.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductSpecifications implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String publicaton_id;
	private String value="-";
}
