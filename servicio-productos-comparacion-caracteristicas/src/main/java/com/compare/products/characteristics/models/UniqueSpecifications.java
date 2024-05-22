package com.compare.products.characteristics.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UniqueSpecifications {

	private String id;
	private List<String> attributes;
}
