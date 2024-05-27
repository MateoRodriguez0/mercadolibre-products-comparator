package com.compare.products.vendedores.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class InfoSeller {
	private String publicacion_id;
	private String value;
}
