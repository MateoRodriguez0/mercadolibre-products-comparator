package com.compare.products.opinions.services.implementations;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.compare.products.opinions.clients.ReviewsClient;
import com.compare.products.opinions.models.Opinion;
import com.compare.products.opinions.services.OpinionService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Scope("prototype")
public class OpinionProductCatalog implements OpinionService{

	@Override
	public Double getRatingAverage(String id) {
		JsonNode publication=getCatalogProduct(id, request.getHeader("Authorization"));
		if(publication!= null) {
			String itemId=publication.at(this.itemId).asText();
			String parentId=publication.at(this.ParentId).asText();
			JsonNode childrens=publication.at(childrenIds);
			if(itemId.length() != 0 & parentId.equals("null") && childrens.size()==0) {
				return getReviews(itemId, 0, request.getHeader("Authorization"))
						.at(rating).asDouble();
			}
			
			if(parentId!="null") {
				JsonNode parentProduct=getCatalogProduct(parentId, request.getHeader("Authorization"));
				childrens=parentProduct.at(childrenIds);
				List<String> ItemsId;
				try {
					ItemsId = getItemsId(childrens, request.getHeader("Authorization"));
					return getRatingsAverageForAllItems
							(ItemsId,request.getHeader("Authorization") );
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(childrens.size()>0){
				try {
					List<String>ItemsId= getItemsId(childrens, request.getHeader("Authorization"));
					return getRatingsAverageForAllItems
							(ItemsId,request.getHeader("Authorization") );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return 0.0;
	}

	@Override
	public List<Opinion> getReviews(String id, String token) {
		JsonNode publication=getCatalogProduct(id,token);
		if(publication!= null) {
			String itemId=publication.at(this.itemId).asText();
			String parentId=publication.at(ParentId).asText();
			JsonNode childrens=publication.at(childrenIds);
			
			if(itemId.length() != 0 & parentId.length() != 0 && childrens.size()==0) {
				return itemService.getReviews(itemId, token);
			}
			if(parentId!="null") {
				JsonNode parentProduct=getCatalogProduct(parentId, token);
				childrens=parentProduct.at(childrenIds);
				return getOpinionsForChildrens(childrens, token);
			}
			
			if(childrens.size()>0){
				return getOpinionsForChildrens(childrens, token);
			}
		}
		
		return null;
	}
	
	public List<Opinion> getOpinionsForChildrens(JsonNode childrens, String token){
		List<Opinion>opinions= new ArrayList<>();
		List<Subtask<List<Opinion>>>substaks=new ArrayList<>();
		try (var scope= new StructuredTaskScope<Collection<Opinion>>()){
			List<String>ItemsId= getItemsId(childrens, token);
			for (String item : ItemsId) {
				substaks.add(scope.fork(()->itemService.getReviews(item, token)));
			}
			scope.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Subtask<List<Opinion>> subtask : substaks) {
			opinions.addAll(subtask.get());
		}
		return opinions;
	}
	
	public JsonNode getCatalogProduct(String id,String token) {
		return client.findCatalogProduct(id, token).getBody();
	}

	
	public JsonNode getReviews(String id,int offset,String token) {
		return client.findReviewsItem(id,offset, token).getBody();
	}
	
	
	
	public List<String> getItemsId(JsonNode Childrens,String token) throws InterruptedException{
		List<String> ids= new ArrayList<>();
		List<Subtask<String>> subtasks= new ArrayList<>();
		
		try (var scope= new StructuredTaskScope<String>()){
			for (JsonNode id : Childrens) {
				subtasks.add(scope.fork(() -> {
					JsonNode product= getCatalogProduct(id.asText(), token);
					return product.at(this.itemId).asText();
				}));	
			}
			scope.join();
			for (Subtask<String> subtask : subtasks) {
				if(subtask.get().length()!=0) {
					ids.add(subtask.get());
				}
			}
		}
		return ids;
	}
	
	
	public Double getRatingsAverageForAllItems(List<String> items,String token) throws InterruptedException {
		List<Double> califications= new ArrayList<>();
		List<Subtask<Double>> subtasks= new ArrayList<>();
		
		try (var scope= new StructuredTaskScope<Double>()){
			for (String itemId : items) {
				subtasks.add(scope.fork(() ->{
					JsonNode review= getReviews(itemId, 0,token );
					return review.at(rating).asDouble();
				}));
			}  
			scope.join();
		}
		subtasks.stream()
		 .filter(task ->task.get()!=0)
		 .forEach(task -> califications.add(task.get()));
		
		DecimalFormat decimalFormat = new DecimalFormat("#0.0");
		decimalFormat.setRoundingMode(RoundingMode.DOWN);
		
		return Double.valueOf(decimalFormat.format(
				califications
				.stream()
				.mapToDouble(value ->value).average()
				.orElse(0)
				));
	}


	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ReviewsClient client;
	
	@Autowired
	@Qualifier("opinionItem")
	OpinionService itemService;
	
	@Value("${json.properties.product_catalog.parent}")
	private String ParentId;	
	
	@Value("${json.properties.product_catalog.children_ids}")
	private String childrenIds;	
	
	@Value("${json.properties.product_catalog.id}")
	private String itemId;
	
	@Value("${json.properties.reviews-item.rating}")
	private String rating;
	
}
