package com.compare.products.characteristics.models;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group  implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String label;
	private List<Attribute> attributes;
}
