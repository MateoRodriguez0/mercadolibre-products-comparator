package com.products.compare.vendedores.models;


import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseTimeItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int totalResponses;
	private int time;
	private int totalQuestions;
}
