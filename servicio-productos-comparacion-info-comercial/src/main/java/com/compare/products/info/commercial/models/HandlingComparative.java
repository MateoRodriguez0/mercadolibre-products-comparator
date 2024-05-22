package com.compare.products.info.commercial.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HandlingComparative {
	
	private String id;
	private String price;
	private String handling_time;
}
