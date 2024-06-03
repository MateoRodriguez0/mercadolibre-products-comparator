package com.compare.products.characteristics.models;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UniqueSpecifications implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private List<String> attributes;
}
