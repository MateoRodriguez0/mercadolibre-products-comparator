package com.compare.products.opinions.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.compare.products.opinions.clients.ReviewsClient;
import com.compare.products.opinions.models.Opinion;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class FutureTaskOpinions implements Supplier<Collection<Opinion>>{
	
	@Override
	public Collection<Opinion> get() {

		List<Opinion> opinions = new ArrayList<>();
		JsonNode Listreviews=  client.findReviewsItem(id,offset, token).getBody();
		for(JsonNode jsonNode : Listreviews.at(reviewsItem)) {
			opinions.add(new Opinion(jsonNode.at(contentReview).asText(),
					jsonNode.at(rateReview).asDouble()));
			}
		return opinions;
	}
	
	//Constuctro All fields
	//(id, offset, client, token, contenReview, rateReview, reviewsItem)
	public FutureTaskOpinions() {
		// TODO Auto-generated constructor stub
	}
	
	private String id;
	private int offset;
	private String token;
	private ReviewsClient client;
	private String contentReview;
	private String rateReview;
	private String reviewsItem;
	
	
}
