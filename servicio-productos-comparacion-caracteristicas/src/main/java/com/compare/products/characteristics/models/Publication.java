package com.compare.products.characteristics.models;

import java.io.Serializable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;

@Data
public class Publication implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

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
