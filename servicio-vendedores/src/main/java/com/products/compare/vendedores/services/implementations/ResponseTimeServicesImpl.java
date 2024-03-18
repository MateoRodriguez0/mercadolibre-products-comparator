package com.products.compare.vendedores.services.implementations;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.products.compare.vendedores.clients.SellerFeignClient;
import com.products.compare.vendedores.services.ResponseTimeServices;
import com.products.compare.vendedores.util.DatesUtil;

@Service
@Scope("prototype")
public class ResponseTimeServicesImpl implements ResponseTimeServices {
	private ObjectNode questions=new ObjectNode(null);
	private int offset=0;
	
	@Override
	public String timeOfResponseBySeller(String itemId) {
		offset=0;
		int totalResponses=0;
		int time=0;
		questions= sellerClients.getQuestionsByItem(itemId, api_version,0)
				.getBody();
		int limit=questions.get("limit").asInt();
		int totalQuestions=questions.get("total").asInt();
		
		if(totalQuestions==0) {
			return "UNQUESTION";
		}
		
		do {
			try(var scope= new StructuredTaskScope<>()){
				Subtask<int []> subtask= scope.fork(() -> timeOfResponseByQuestions(questions));
				Subtask<ObjectNode>subtask2=null;
				
				offset+=50;
				
				if(totalQuestions>offset && totalQuestions <(offset+limit)) {
					subtask2=scope.fork(()->
					sellerClients.getQuestionsByItem(itemId, api_version,offset).getBody());
				}
				scope.join();
				totalResponses+=subtask.get()[0];
				time+=subtask.get()[1];
				if(subtask2!=null)
					questions=subtask2.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}while(totalQuestions>offset && totalQuestions <(offset+limit));

		if(totalResponses==0) {
			return "UNANSWERED";
		}
		
		return formatResponseTime( totalResponses, time, 
				Duration.ofMinutes(time/totalResponses));
	}

	public int[] timeOfResponseByQuestions(ObjectNode questions) {
		int totalResponses=0;
		int time=0;
		
		for (JsonNode q : questions.get("questions")) {
			if(q.get("status").asText().equalsIgnoreCase("ANSWERED")) {
				totalResponses++;
				time+= DatesUtil.getMinutes(q.get("date_created").asText(), 
						q.get("answer").get("date_created").asText());
				}
			}
		int[] array={totalResponses,time};
		return array;
	   
	}
	
	private String formatResponseTime( int totalResponses, int time, Duration duracion) {
		if(time/totalResponses <60 && time/totalResponses >0) {
			return duracion.toMinutes()+" minutos";
		}
		else if(time/totalResponses >60&& time/totalResponses < 1440 ) {
			Long minutos=duracion.toMinutes()-duracion.toHours()*60;
			return duracion.toHours()+" horas con "+minutos+" minutos";	
		}
		else if(time/totalResponses >=1440){
			return "Mas de 24 horas";
		}
		return null;
		
	}


	@Autowired
	private SellerFeignClient sellerClients;
	
	
	@Value("${compare.products.api.version}")
	private String api_version;
	
}