package com.compare.products.characteristics.models;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Product {
	private String id;
	public JsonNode attributes;
}
