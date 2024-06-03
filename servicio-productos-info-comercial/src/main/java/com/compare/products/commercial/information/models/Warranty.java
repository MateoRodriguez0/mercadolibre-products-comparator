package com.compare.products.commercial.information.models;


import java.io.Serializable;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Warranty implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int number;
    private String unit;
}


