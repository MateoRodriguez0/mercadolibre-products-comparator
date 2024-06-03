package com.compare.products.models;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Publication {

	public Publication(JsonNode publication, String type) {
		this.publication = publication;
		this.type = type;
		publicationType=PublicationType.valueOf(type);
		Publication_id=getPublication().get("id").asText();
	}
	

	private JsonNode publication;
	private String type;
	private PublicationType publicationType;
	private String Publication_id;
	

}
