package com.compare.products.characteristics.services;

import java.util.List;
import java.util.Set;

import com.compare.products.characteristics.models.Attribute;
import com.compare.products.characteristics.models.Group;
import com.compare.products.characteristics.models.Publication;
import com.fasterxml.jackson.databind.JsonNode;

public interface CharacteristicsService {

	public JsonNode getCharacteristics(Publication publication);
	
	public Set<Group> searchCommonTechnicalSpecifications(String [] categories);
	
	public List<Attribute> searchSharedAttributes(List<Publication>publications);
	
	public List<Attribute> searchUniqueAttributes(List<Attribute> attributes,Publication publication);
		
}
