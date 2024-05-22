package com.compare.products.characteristics.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group  {
	private String label;
	private List<Attribute> attributes;
}
