package com.products.compare.vendedores.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seller {

	private String id;
	private String nickname;
	private String permalink;
	private String mercadoLider_level;
	private int total_transactions;
	private double positive_rating;
	private double negative_rating;
	private double neutral_rating;
	private String experience;
	private String location;
	private JsonNode metrics;
	private String response_time;
}
