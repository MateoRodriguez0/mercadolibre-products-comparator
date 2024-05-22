package com.products.compare.vendedores.models;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseTimeItem {

	private int totalResponses;
	private int time;
	private int totalQuestions;
}
