package com.compare.products.characteristics.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Specifications {
	private List<UniqueSpecifications> uniques_specifications;
	private List<Attribute> shared_attributes;
}
