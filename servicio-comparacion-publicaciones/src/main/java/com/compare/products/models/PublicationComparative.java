package com.compare.products.models;

import java.io.Serializable;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicationComparative implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JsonNode info_comercial;
	private JsonNode specifications;
	private JsonNode analisis_opinions;
	private JsonNode analisis_description;
	private JsonNode info_sellers;

}
