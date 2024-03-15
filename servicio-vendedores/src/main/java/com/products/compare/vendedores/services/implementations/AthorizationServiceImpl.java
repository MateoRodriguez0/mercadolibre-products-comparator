package com.products.compare.vendedores.services.implementations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.products.compare.vendedores.services.AuthorizathionService;

@Service
@Scope("singleton")
public class AthorizationServiceImpl implements AuthorizathionService{

	private String bearer=null;
	
	@Override
	public String getToken() {
		return bearer;
	}

	@Override
	public void SetToken(String bearer) {
		this.bearer=bearer;
	}

}
