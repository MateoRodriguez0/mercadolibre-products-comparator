package com.compare.products.characteristics.services.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compare.products.characteristics.clients.ApiCategoriesClient;
import com.compare.products.characteristics.models.Attribute;
import com.compare.products.characteristics.models.Group;
import com.compare.products.characteristics.models.Publication;
import com.compare.products.characteristics.models.PublicationType;
import com.compare.products.characteristics.services.CharacteristicsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


@Service
@Scope("prototype")
public class CharacteristicsServiceImpl implements CharacteristicsService  {

	@Override
	public JsonNode getCharacteristics(Publication publication) {
		return publication.getPublication().at(attributes);
	}

	
	@Override
	public List<Attribute> searchSharedAttributes(List<Publication> publications){
		AtomicReference<List<String>> atts=new AtomicReference<>(new ArrayList<>());
		List<Attribute> sharedAtts= new ArrayList<>();
		
		for (Publication publication : publications) {
			for (JsonNode att : publication.getPublication().at(attributes)) {
				String id=att.at(idAttribute).asText();	
				if(!filterAttributes(id)) {
					atts.get().add(att.at("/name").asText());
				}
			}
		}
		Set<String> atts2=atts.get().parallelStream()
				.filter(a ->{
					return atts.get().parallelStream().filter(a2 ->a2.equals(a))
							.toList().size()>1;})
				.collect(Collectors.toSet());
		
		atts2.forEach(a ->sharedAtts.add(Attribute.builder().name(a).build()));
		return sharedAtts;
	}

	@Override
	public Set<Group> searchCommonTechnicalSpecifications(String[] categories) {
		Set<Group> groups= new HashSet<>();
		try(var scope= new StructuredTaskScope<>()){
			for (String id : categories) {
				scope.fork(() -> {
					JsonNode specs =searchSpecifications(id);
					for (JsonNode s : specs.at(this.groups)) {
						Group group= new Group();
						group.setLabel(s.at(groupLabel).asText());
						List<Attribute> attributes=new ArrayList<>();
						group.setAttributes(attributes);
				
						for (JsonNode comp : s.at(groupComponents)) {
							if(!filterAttributes(comp.at(attributesComponent)
											.get(0).at(idAttribute).asText())) {
								attributes.add(createAttribute(comp));		
							}
						}
						groups.add(group);
					}
					return null;
				});	
			}	
			scope.join();
		} catch (InterruptedException e) {}
		return groups;
	}
	
	
	@Override
	public List<Attribute> searchUniqueAttributes(List<Attribute> attr,Publication publ){
	    Map<String, Boolean> shared = new HashMap<>();
	    attr.forEach(att ->shared.put(att.getName(), true) );
	    List<Attribute> attributes=new ArrayList<>();
	    try(var scope= new StructuredTaskScope<>()){
	    	Subtask<JsonNode> task1= scope.fork(() ->getCharacteristics(publ));
	    	Subtask<Map<String, String>> task2= scope.fork(() -> getIgnoreAttributes(publ));
	    	scope.join();
	    	for (JsonNode at : task1.get()) {
		    	String id=at.at(idAttribute).asText();
		    	 if(!filterAttributes(id)&& ! task2.get().containsKey(at.at("/id").asText())){
		    		if (!shared.containsKey(at.at(nameAttribute).asText())){
		    			Attribute attribute= Attribute.builder()
								.name(at.at(nameAttribute).asText())
								.value(at.at(valueName).asText())
								.build();
						attributes.add(attribute);
			        }
		    	}
		    } 
	    } catch (InterruptedException e) {
			e.printStackTrace();
		}
	    
	    return attributes;
	}
	
	private Attribute createAttribute(JsonNode comp) {
		Attribute attribute= Attribute.builder()
				.label(comp.at(labelAttribute).asText())
				.id(comp.at(attributesComponent).get(0).at(idAttribute).asText())
				.name(comp.at(attributesComponent).get(0).at(nameAttribute).asText())
				.build();
		attribute.CompleteName();
		return attribute;
		
	}
	

	private Map<String, String> getIgnoreAttributes(Publication publ){
		Map<String, String> atri= new HashMap<>();
		try(var scope= new StructuredTaskScope<>() ){
			scope.fork(() -> {
				ArrayNode atts=null;
				if(publ.getPublicationType()==PublicationType.catalog_product) {
					atts=client.getAttributesByCategory(publ.getPublication()
							.at("/buy_box_winner/category_id").asText())
							.getBody().deepCopy();
				}
				else{
					atts=client.getAttributesByCategory(publ.getPublication()
							.at("/category_id").asText())
							.getBody().deepCopy();
				}
				for (JsonNode at : atts) {
					if (at.at("/hierarchy").asText().equals("ITEM") ||
							at.at("/hierarchy").asText().equals("PRODUCT_IDENTIFIER")
							|| at.at("/hierarchy").asText().equals("CHILD_PK")) {
					 	atri.put(at.at("/id").asText(), null);

					 	
					}
				}
				return true;
				});
			scope.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return atri;
	}
	
	
	public JsonNode searchSpecifications(String id) {
		ResponseEntity<JsonNode> response=client.technicalSpecs(id);
		return response.getBody();
	}
	
	private boolean filterAttributes(String id) {
		boolean isAtIgnore=false;
		for (String at : attributesIgnore) {
			if(at.equals(id)) {
				isAtIgnore=true;
			break;
			}
		}
		return isAtIgnore;
	}
	
	@Autowired
	private ApiCategoriesClient client;

	@Value("${json.properties.technical_specs.attribute.label}")
	private String labelAttribute;
	
	@Value("${json.properties.technical_specs.attribute.name}")
	private String nameAttribute;
	
	@Value("${json.properties.technical_specs.attribute.id}")
	private String idAttribute;
	
	@Value("${json.properties.technical_specs.group_label}")
	private String groupLabel;
	
	@Value("${json.properties.technical_specs.group_components}")
	private String groupComponents;
	
	@Value("${json.properties.technical_specs.groups}")
	private String groups;
	
	@Value("${json.properties.technical_specs.component.attributes}")
	private String attributesComponent;
	
	@Value("${json.properties.product_and_item_attributes}")
	private String attributes;
	
	@Value("${json.properties.technical_specs.attribute.ignore}")
	private List<String> attributesIgnore;
	
	@Value("${json.properties.product_and_item_attribute.value_name}")
	private String valueName;
	
}