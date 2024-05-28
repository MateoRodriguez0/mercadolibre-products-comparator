package com.compare.products.info.commercial.models;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Warranty{
	private int number;
    private String unit;
}


