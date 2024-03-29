package com.compare.products.commercial.information.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.compare.products.commercial.information.models.CommercialInformation;
import com.compare.products.commercial.information.models.PublicationType;
import com.compare.products.commercial.information.services.InformationCommercialService;
import com.compare.products.commercial.information.services.PaymentMethodsService;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Primary
public class InformationCommercialServiceImpl implements InformationCommercialService{

	@Override
	public CommercialInformation getInfoCommercial(JsonNode jsonNode, String token, PublicationType type) {
	
		CommercialInformation information = new CommercialInformation();
		information.setDiscount(getDiscount(jsonNode, type));
		System.out.println(jsonNode.at("/attributes/id: 'BRAND'"));
		if(type==PublicationType.item) {
			
		}
		information.setPayment_methods(paymentMethodsService.findPaymentMethods(token));
		return information;
	}
	
	@Override
	public CommercialInformation infoCommercialForItemType(JsonNode jsonNode, String token) {
		
		return null;
	}




	@Override
	public CommercialInformation infoCommercialForProductType(JsonNode jsonNode, String token) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getDiscount(JsonNode jsonNode, PublicationType type) {
		if(type==PublicationType.item) {
			int orignalPrice=jsonNode.get(ItemPriceOriginal).asInt();
			int currentPrice=jsonNode.get(ItemPrice).asInt();
			if(orignalPrice!=0) {
				return (int)(((double)(orignalPrice-currentPrice)/orignalPrice)*100);
			}
			return 0;
		}
		else{
			int orignalPrice=jsonNode.at(ProductPriceOriginal).asInt();
			int currentPrice=jsonNode.at(ProductPrice).asInt();
			if(orignalPrice!=0) {
				return (int)(((double)(orignalPrice-currentPrice)/orignalPrice)*100);
			}
			return 0;
		}
		
	}

	

	@Autowired
	private PaymentMethodsService paymentMethodsService;

	@Value("${json.properties.item.price}")
	private String ItemPrice;
	
	@Value("${json.properties.item.original_price}")
	private String ItemPriceOriginal;

	@Value("${json.properties.product_catalog.price}")
	private String ProductPrice;
	
	@Value("${json.properties.product_catalog.original_price}")
	private String ProductPriceOriginal;



	

}
