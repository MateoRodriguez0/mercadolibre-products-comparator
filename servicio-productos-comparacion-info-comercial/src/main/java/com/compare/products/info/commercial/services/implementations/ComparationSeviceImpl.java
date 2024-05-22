package com.compare.products.info.commercial.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.commons.models.CommercialInformation;
import com.compare.products.commons.models.Publication;
import com.compare.products.commons.models.ShippingCost;
import com.compare.products.commons.models.ShippingMode;
import com.compare.products.commons.models.Warranty;
import com.compare.products.info.commercial.clients.InfoCommercialClient;
import com.compare.products.info.commercial.models.ComparativeInformationCommercial;
import com.compare.products.info.commercial.models.HandlingComparative;
import com.compare.products.info.commercial.models.InfoCommercialProdcut;
import com.compare.products.info.commercial.models.ShippingComparative;
import com.compare.products.info.commercial.services.ComparationServices;
import com.compare.products.info.commercial.services.ShippingComparationService;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Scope("prototype")
public class ComparationSeviceImpl implements ComparationServices {

	@Override
	public ComparativeInformationCommercial compare(List<Publication> publications) {
		String token= request.getHeader("Authorization");
		ComparativeInformationCommercial comparative=
							new ComparativeInformationCommercial();
		List<CommercialInformation> informations= new ArrayList<>();
		
		try(var scope= new StructuredTaskScope<>()){
			for (Publication p : publications) {
				scope.fork(() ->{
					String id=p.getPublication_id();
					CommercialInformation info=client
							.getInformationCommercial(
								p.getPublication(),p.getPublicationType(),token).getBody();
					System.out.println(info);
					info.setId(id);
					comparative.getBrand().add(new InfoCommercialProdcut(id,
							info.getBrand()));
					comparative.getDiscount_porcentage().add(new InfoCommercialProdcut(id,
							getDiscount(info.getDiscount_porcentage())));
					comparative.getAvailables().add(new InfoCommercialProdcut(id,
							info.getAvailables()));
					comparative.getRating_average().add(new InfoCommercialProdcut(id, 
							info.getRating_average()+""));
					comparative.getInternational_delivery_mode()
							.add(new InfoCommercialProdcut(id,isInternationalDelivery(info)));
					comparative.getPayment_methods().add(new InfoCommercialProdcut(id,
							info.getPayment_methods()));
					comparative.getPrice().add(new InfoCommercialProdcut(id,
							"$ "+info.getPrice()+" "+info.getCurrency_id()));
					comparative.getWarranty().add(new InfoCommercialProdcut(id, 
							getWarranaty(info.getWarranty())));
			
					informations.add(info);
					return info;
				});
				
			}
			scope.join();
			boolean containsHandlingInfo=shippingService.existHandlingTime(informations);
			if(containsHandlingInfo) {
				comparative.setShipping_comparative(new ArrayList<>());
				shippingService.getAddress(informations).forEach(a -> {
					ShippingComparative ship= new ShippingComparative();
					ship.setCity(a);
					comparative.getShipping_comparative().add(ship);	
				});

				informations.forEach(t -> {
					if(t.getShipping().getHandling_costs()!=null) {
						t.getShipping().getHandling_costs().forEach(h ->{
							comparative.getShipping_comparative().forEach(ship ->{
								if(ship.getCity().equals(h.getAddress())) {
									ship.getProducts().add(gethandlingComparative(t,h));
								}
							 });
						});
					}
					else {
						comparative.getShipping_comparative().forEach(ship ->{
							ship.getProducts().add(gethandlingComparative(t,null));
						});
					}
				});
			}
			if(shippingService.containtShippingFree(informations)&&!containsHandlingInfo){
				comparative.setShipping(new ArrayList<>());
				informations.forEach(com ->{
					comparative.getShipping().add(getShipping(com));
				});
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return comparative;
	}

	private HandlingComparative gethandlingComparative(CommercialInformation t, 
			ShippingCost cost) {
		String handling=cost!=null? shippingService.getdeliveryTime(cost):"-";
		
		return HandlingComparative
				.builder()
				.id(t.getId())
				.price(t.getShipping().getMode()==ShippingMode.free ? "Gratis" : "-")
				.handling_time(handling)
				.build();
	}
	
	private String getWarranaty(Warranty w) {
		if(w!=null) {
			return w.getNumber()+" "+ w.getUnit();
		}
		return "-";
	}
	

	private String isInternationalDelivery(CommercialInformation info) {
		return info.getInternational_delivery_mode().equals("none")
				||info.getInternational_delivery_mode().length()==0? "NO":"Si";
	}
	
	private String getDiscount(int d) {
		return d>=0? d+"%" : "No tiene";
	}
	
	private InfoCommercialProdcut getShipping(CommercialInformation com) {
		return new InfoCommercialProdcut(com.getId(),
				com.getShipping().getMode()==ShippingMode.free? "Gratis":"-");
	}

	@Autowired
	private InfoCommercialClient client;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired 
	private ShippingComparationService shippingService;
}
