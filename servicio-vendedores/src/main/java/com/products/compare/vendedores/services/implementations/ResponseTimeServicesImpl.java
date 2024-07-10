package com.products.compare.vendedores.services.implementations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.products.compare.vendedores.clients.SellerFeignClient;
import com.products.compare.vendedores.models.ResponseTime;
import com.products.compare.vendedores.models.ResponseTimeItem;
import com.products.compare.vendedores.services.ResponseTimeServices;
import com.products.compare.vendedores.util.DatesUtil;

@Service
@Scope("prototype")
@RefreshScope
public class ResponseTimeServicesImpl implements ResponseTimeServices {
	
	
	@Override
	public String timeOfResponseBySeller(String[] itemsId) {
		List<ResponseTimeItem> responsesTime= new ArrayList<>();
		int totalResponses=0;
		int time=0;
		int totalQuestions=0;
		int durationAvg=0;
		int totalItems=0;
		try(var scope= new StructuredTaskScope<>()){
			for (String r : itemsId) {
				scope.fork(() -> {
					responsesTime.add(timeOfResponseBySeller(r));
					return true;
				});
			}
			scope.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (ResponseTimeItem r : responsesTime) {
			totalResponses+=r.getTotalResponses();
			totalQuestions+=r.getTotalQuestions();
			time+=r.getTime();
			if(r.getTotalResponses()!=0) {
				durationAvg+=r.getTime()/r.getTotalResponses();
				totalItems++;
			}
			
			
		}
		
		if(totalQuestions==0) {
			return ResponseTime.UNQUESTION.name();
		}
		else if(totalQuestions!=0&&totalResponses==0) {
			return ResponseTime.UNANSWERED.name();
		}
		return formatResponseTime( totalResponses,time, 
				Duration.ofMinutes(durationAvg/totalItems));
	}
	
	@Override
	public ResponseTimeItem timeOfResponseBySeller(String itemId) {
		AtomicReference<ObjectNode> questions=new AtomicReference<>(new ObjectNode(null));
		AtomicInteger offset=new AtomicInteger(0);
		int totalResponses=0;
		int time=0;
		questions.set(sellerClients.getQuestionsByItem(itemId, api_version,0)
				.getBody());
		int limit=questions.get().get("limit").asInt();
		int totalQuestions=questions.get().get("total").asInt();
		if(totalQuestions==0) {
			return ResponseTimeItem.builder()
					.totalQuestions(0)
					.time(0)
					.totalResponses(0)
					.build();
		}
		try (var scope = new StructuredTaskScope<>()) {
			do {
				Subtask<int[]> subtask = scope.fork(() -> 
						timeOfResponseByQuestions(questions.get()));
				Subtask<ObjectNode> subtask2 = null;
				offset.set(offset.get()+50);

				if (totalQuestions > offset.get() && totalQuestions < (offset.get() + limit)) {
					subtask2 = scope.fork(() ->
							sellerClients.getQuestionsByItem(itemId, api_version, offset.get())
							.getBody());
				}
				scope.join();
				totalResponses += subtask.get()[0];
				time += subtask.get()[1];
				if (subtask2 != null)
					questions.set(subtask2.get());
			} while (totalQuestions > offset.get() && totalQuestions < (offset.get() + limit));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		return ResponseTimeItem.builder()
				.time(time)
				.totalQuestions(totalQuestions)
				.totalResponses(totalResponses)
				.build();
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