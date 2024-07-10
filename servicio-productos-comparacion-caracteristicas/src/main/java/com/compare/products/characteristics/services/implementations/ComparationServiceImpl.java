package com.compare.products.characteristics.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.characteristics.clients.CategoriesClient;
import com.compare.products.characteristics.models.Attribute;
import com.compare.products.characteristics.models.Group;
import com.compare.products.characteristics.models.Product;
import com.compare.products.characteristics.models.ProductSpecifications;
import com.compare.products.characteristics.models.Publication;
import com.compare.products.characteristics.models.PublicationType;
import com.compare.products.characteristics.models.Specifications;
import com.compare.products.characteristics.models.UniqueSpecifications;
import com.compare.products.characteristics.services.CharacteristicsService;
import com.compare.products.characteristics.services.ComparationServices;
import com.compare.products.characteristics.services.UniqueCharacteristicsServices;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletResponse;

@Service
@Primary
@Scope("prototype")
@RefreshScope
public class ComparationServiceImpl implements ComparationServices {

	@Override
	public Object compare(List<Publication> publications) {
		String [] ids=publications.stream().map(p ->{
			if(p.getPublicationType()==PublicationType.item) {
				return p.getPublication().at(itemCategoryId).asText();
				}
			return p.getPublication().at(productCategoryId).asText();
		}).toArray(String[]::new);
		
		boolean sameDomaim=client.compatibleToCompare(ids).getBody();
		if(!haveAttributes(publications)) {
			return "WHIT_OUT_SPECS";
		}
		if(sameDomaim) {
			response.addHeader("compatibility","COMMON_DOMAINS");
			return commonDomains(publications, ids);
			
		}
		else {
			return noCommonDomains(publications);
		}		
		
	}
	
	private Specifications noCommonDomains(List<Publication> publications) {
		Specifications specifications= 
				new Specifications(null,crtsService.searchSharedAttributes(publications));
		for (Attribute att : specifications.getShared_attributes()) {
			att.setValues(new ArrayList<>());
			for (Publication p : publications) {
				for (JsonNode node : crtsService.getCharacteristics(p)) {
					if(node.at(nameAttribute).asText().equals(att.getName())) {
						ProductSpecifications product= new ProductSpecifications();
						product.setPublicaton_id(p.getPublication().at(publicationId).asText());
						if(!node.at(valueName).asText().equals("null")) {
							product.setValue(node.at(valueName).asText());
						}
						att.getValues().add(product);
					}	
				}
			}
		}
		List<UniqueSpecifications> uniquesSpecifications=new ArrayList<>();
		for (Publication publication : publications) {
			UniqueSpecifications uSpec= new UniqueSpecifications();
			List<String> attributes=crtsService.searchUniqueAttributes
					(specifications.getShared_attributes(), publication).stream()
					.filter(a ->!a.getValue().equals("No")&& !a.getValue().equals("null"))
					.map(a -> unicrtsService.CompletePhrase(a)).toList();
			
			uSpec.setId(publication.getPublication().at(publicationId).asText());
			uSpec.setAttributes(attributes);
			uniquesSpecifications.add(uSpec);
		
		}
		specifications.setUniques_specifications(uniquesSpecifications);
		
		
		return specifications;
	}

	private Set<Group> commonDomains(List<Publication> publications, String[] ids) {
		Set<Group> groups=crtsService.searchCommonTechnicalSpecifications(ids);
		List<Product> attributes=publications.parallelStream().map(p ->{
			Product prod= new Product(p.getPublication().at(publicationId).asText(),
					crtsService.getCharacteristics(p));
			return prod;
		}).toList();
		
		for (Group group : groups) {
			for (Attribute att : group.getAttributes()) {
				List<ProductSpecifications> values= new ArrayList<>();
				att.setValues(values);
				for (Product productAttributes : attributes) {
					ProductSpecifications product= 
							new ProductSpecifications();
					product.setPublicaton_id(productAttributes.getId());
					for (JsonNode nodeAttributes : productAttributes.getAttributes()) {
						if(nodeAttributes.at(idAttribute).asText().equals(att.getId())){
							product.setValue(nodeAttributes.at(valueName).asText());	
						}
						
					}
					values.add(product);
				}
			
			}
		}

		return groups.parallelStream().map(g ->{
			g.setAttributes(g.getAttributes().stream().filter(a ->a.inPublication())
			.toList());
			return g;
			
		}).filter(g ->g.getAttributes().size()!=0).collect(Collectors.toSet());
	}
	
	
	public boolean haveAttributes(List<Publication> publications) {
		int total=0;
		for (Publication publication : publications) {
			total+=publication.getPublication().at(attributes).size();
		}
		return total !=0;
	}
	
	
	
	
	
	@Autowired
	private CategoriesClient client;
	
	@Value("${json.properties.item.category_id}")
	private String itemCategoryId;
	
	@Value("${json.properties.product_catalog.category_id}")
	private String productCategoryId;
	
	@Value("${json.properties.technical_specs.attribute.id}")
	private String idAttribute;
	
	@Value("${json.properties.product_and_item_attribute.value_name}")
	private String valueName;
	
	@Value("${json.properties.technical_specs.attribute.name}")
	private String nameAttribute;
	
	@Autowired
	private CharacteristicsService crtsService;
	
	@Value("${json.properties.product_and_item_attributes}")
	private String attributes;
	
	@Value("${json.properties.product_and_item_attribute.id}")
	private String publicationId;

	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private UniqueCharacteristicsServices unicrtsService;
}
