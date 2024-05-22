package com.compare.products.info.commercial.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.commons.models.CommercialInformation;
import com.compare.products.commons.models.ShippingCost;
import com.compare.products.commons.models.ShippingMode;
import com.compare.products.info.commercial.util.DateUtil;

@Service
@Scope("prototype")
public class ShippingComparationService {

	public boolean containtShippingFree(List<CommercialInformation> com) {
		return com.parallelStream().filter(c -> c.getShipping()
				.getMode()==ShippingMode.free)
				.toList().size()!=0;
	}
	
	public boolean existHandlingTime(List<CommercialInformation> com) {
		return com.parallelStream()
				.filter(c-> c.getShipping().getHandling_costs()!=null)
				.filter(c-> c.getShipping().getHandling_costs().size()!=0)
				.findFirst().isPresent();
	}
	
	public String getdeliveryTime(ShippingCost cost ) {
		if(!cost.getEstimated_delivery_time().equals("null")) {
			LocalDateTime date= DateUtil.textTodate(cost.getEstimated_delivery_time());
			LocalDateTime offset= DateUtil.textTodate(cost.getEstimated_delivery_time_offset());
			return "Entre "+ offset.toLocalDate()+" y "+date.toLocalDate();
		}
		else {
			LocalDateTime offset= DateUtil.textTodate(cost.getEstimated_delivery_time_offset());
			return "A partir del "+ offset.toLocalDate();
		}
	}
	
	public Set<String> getAddress(List<CommercialInformation> commercialInformations){
		return commercialInformations.parallelStream()
				.filter(com -> com.getShipping().getHandling_costs()!=null)
				.map(co ->co.getShipping().getHandling_costs())
				.flatMap(ship -> ship.stream())
				.map(s ->s.getAddress())
				.collect(Collectors.toSet());
	}
}
