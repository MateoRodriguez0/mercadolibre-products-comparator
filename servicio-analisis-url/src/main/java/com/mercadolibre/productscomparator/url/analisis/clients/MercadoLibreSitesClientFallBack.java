package com.mercadolibre.productscomparator.url.analisis.clients;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class MercadoLibreSitesClientFallBack implements MercadoLibreSitesClient {

	@Override
	public List<JsonNode> getPaises() {
		
		List<String> paises=List.of("MRD","MPA","MCU","MLU","MBO","MLA","MCR","MCO",
				"MEC","MPE","MLV","MLC","MPY","MNI","MHN","MSV","MLM","MGT","MLB");
		
		ObjectMapper mapper = new ObjectMapper();
		return paises
				.stream()
				.map(pais ->((ObjectNode)mapper.createObjectNode()).put("id", pais))
				.collect(Collectors.toList());
	}

}
