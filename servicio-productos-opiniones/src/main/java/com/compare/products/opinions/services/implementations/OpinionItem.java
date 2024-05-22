package com.compare.products.opinions.services.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Service;

import com.compare.products.opinions.clients.ReviewsClient;
import com.compare.products.opinions.models.Opinion;
import com.compare.products.opinions.services.FutureTaskOpinions;
import com.compare.products.opinions.services.OpinionService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Scope("prototype")
public class OpinionItem implements OpinionService {

	@Override
	public Double getRatingAverage(String id) {
		JsonNode reviews=getReviews(id,0, request.getHeader("Authorization"));
		return reviews != null? reviews.at(rating).asDouble(): null;
	}

	@Override
	public List<Opinion> getReviews(String id,String token) {
		List<Opinion> opinions = new ArrayList<>();
		JsonNode Listreviews= getReviews(id,0, token);
		
		int total=Listreviews.at(totalReviews).asInt();
		int limit=Listreviews.at(limitReviews).asInt();
		int offset=0; 
		
		for (JsonNode jsonNode : Listreviews.at(reviewsItem)) {
			opinions.add(new Opinion(jsonNode.at(contentReview).asText(),
					jsonNode.at(rateReview).asDouble()));
		}
		
	    if (total > limit) {
	    	List<CompletableFuture<Collection<Opinion>>> futures= new ArrayList<>();
	        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor(); 

	        if(ischildren) {
	        	while (limit<=total & total-(limit+offset)>0) {
		        	offset += limit;
		            futures.add(CompletableFuture.supplyAsync(new FutureTaskOpinions
		            		(id,offset,token,client, contentReview, rateReview, reviewsItem),
		            		 executor));
		            if(offset==10 && total >15) {
		            	offset=total-limit;
		            }
		            }
	        }
	        else {
	        	while (limit<=total & total-(limit+offset)>0) {
		        	offset += limit;
		            futures.add(CompletableFuture.supplyAsync(new FutureTaskOpinions
		            		(id,offset,token,client, contentReview, rateReview, reviewsItem),
		            		 executor));
		            }
	        }
	        
	        for (CompletableFuture<Collection<Opinion>> future : futures) {
	            try {
					opinions.addAll(future.get());
				} catch ( Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        executor.shutdown();
	    }
	    return opinions;
	}
	
	public JsonNode getReviews(String id,int offset,String token) {
		return client.findReviewsItem(id,offset, token).getBody();
	}

	@Autowired
	private ReviewsClient client;
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("${json.properties.reviews-item.rating}")
	private String rating;	
	
	@Value("${json.properties.reviews-item.total}")
	private String totalReviews;
	
	@Value("${json.properties.reviews-item.reviews}")
	private String reviewsItem;
	
	@Value("${json.properties.reviews-item.limit}")
	private String limitReviews;	
	
	@Value("${json.properties.reviews-item.content}")
	private String contentReview;	
	
	@Value("${json.properties.reviews-item.rate-review}")
	private String rateReview;	
	
	private boolean ischildren=false;
	
	public void setIschildren(boolean ischildren) {
		this.ischildren = ischildren;
	}
	
}
