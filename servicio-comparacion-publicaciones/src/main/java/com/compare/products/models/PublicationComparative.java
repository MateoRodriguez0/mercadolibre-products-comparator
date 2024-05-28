package com.compare.products.models;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicationComparative {
	private JsonNode info_comercial;
	private JsonNode specifications;
	private JsonNode analisis_opinions;
	private JsonNode analisis_description;
	private JsonNode info_sellers;

}
