package com.compare.products.info.commercial.models;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;

@Data
public class Publication {

	public Publication(ObjectNode publication, String type) {
		this.publication = publication;
		this.type = type;
		publicationType=PublicationType.valueOf(type);
		Publication_id=getPublication().get("id").asText();
	}
	

	private ObjectNode publication;
	private String type;
	private PublicationType publicationType;
	private String Publication_id;
	

}
