package com.compare.products.characteristics.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Attribute {
	@JsonIgnore
	private String id;
	private String name;
	@JsonIgnore
	private String label;
	@JsonIgnore
	private String value;
	private List<ProductSpecifications> values;
	
	public void CompleteName() {
		if(!label.equals(name)) {
			name=label+" "+name.toLowerCase();
		}
	}

	public boolean inPublication() {
		boolean in=false;
		for (ProductSpecifications produ : values) {
			if(!produ.getValue().equals("-")) {
				in= true;
			}
		
		}
		return in;
	}
}
