package com.mercadolibre.productscomparator.vendedores.services.implementations;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mercadolibre.productscomparator.vendedores.clients.SellerFeignClient;
import com.mercadolibre.productscomparator.vendedores.services.ResponseTimeServices;
import com.mercadolibre.productscomparator.vendedores.util.DatesUtil;

@Service
public class ResponseTimeServicesImpl implements ResponseTimeServices {

	@Override
	public String timeOfResponseBySeller(String itemId) {
		String ResponseTime="UNQUESTION";
		int offset=0;
		int totalResponses=0;
		boolean responde=false;
		int time=0;
		ObjectNode questions= sellerClients.getTimeOfResponse(itemId, "4",offset).getBody();
		
		if(questions.get("total").asInt()==0) {
			return ResponseTime;
		}
		
		do {
			for (JsonNode q : questions.get("questions")) {
				if(q.get("status").asText().equalsIgnoreCase("ANSWERED")) {
					totalResponses++;
					responde=true;
					String date_created= q.get("date_created").asText();
					String answer_date= q.get("answer").get("date_created").asText();
					time+= DatesUtil.getMinutes(date_created, answer_date);
					}
				}
			offset+=50;
			questions= sellerClients.getTimeOfResponse(itemId, "4",offset).getBody();
		}while(questions.get("questions").size()!=0);

		
		
		if(!responde) {
			ResponseTime="UNANSWERED";
			return ResponseTime;
		}
		
		Duration duracion = Duration.ofMinutes(time/totalResponses);
		
		if(time/totalResponses <60 && time/totalResponses >0) {
			ResponseTime=duracion.toMinutes()+" minutos";
		}
		else if(time/totalResponses >60&& time/totalResponses < 1440 ) {
			Long minutos=duracion.toMinutes()-duracion.toHours()*60;
			ResponseTime=duracion.toHours()+" horas con "+minutos+" minutos";	
		}
		else if(time/totalResponses >=1440){
			ResponseTime="Mas de 24 horas";
		}
		return ResponseTime;
	}

	@Autowired
	private SellerFeignClient sellerClients;
}
