package com.compare.products.vendedores.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class InfoSeller implements Serializable{

	private static final long serialVersionUID = 1L;
	private String publicacion_id;
	private String value;
}
