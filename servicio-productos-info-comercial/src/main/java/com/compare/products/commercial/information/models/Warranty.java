package com.compare.products.commercial.information.models;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Warranty{
	private int number;
    private String unit;
}


