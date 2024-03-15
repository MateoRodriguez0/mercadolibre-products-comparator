package com.products.compare.vendedores.services;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ResponseTimeServices {

	public String timeOfResponseBySeller(String itemId,ObjectNode questions);
}
