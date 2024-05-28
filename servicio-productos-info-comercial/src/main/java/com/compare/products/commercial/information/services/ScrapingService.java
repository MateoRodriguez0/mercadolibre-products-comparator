package com.compare.products.commercial.information.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope("prototype")
public class ScrapingService {

	public String getSales(CompletableFuture<Document> doc){
		String sales=null;
		try {
			sales=doc.get().getElementsByClass(salesTag)
						.text();
			if(sales.contains("mil")) {
				sales=sales.replaceAll("[a-zA-Z]|\s|\\|","").concat("mil");
			}
			else {
				sales=sales.replaceAll("[a-zA-Z]|\s|\\|","");
			}
		} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
		}
		return sales;
	}
	
	public String getAvailables(CompletableFuture<Document> doc){
		String available=null;
		try {
			available= doc.get().getElementsByClass(availableTag).text();
			if(available.contains("mil")) {
				available=available.replaceAll("[a-zA-Z]|\s|\\|","").concat("mil");
			}
			else {
				available=available.replaceAll("[a-zA-ZÀ-ÖØ-öø-ÿ]|\s|[(]|[)]","");			
				}
		} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
		}
		return available;
	}
	
	
	public Document getDocument(String permalink) {
		try {
			return Jsoup.connect(permalink).get();
		} catch (IOException e) {
			return null;
		}
	}
	

	@Value("${compare.products.scraper.tags.quantity-available}")
	private String availableTag;
	
	@Value("${compare.products.scraper.tags.review-calification}")
	private String calificationTag;
	
	@Value("${compare.products.scraper.tags.sales}")
	private String salesTag;
	
	@Value("${compare.products.paths.rating-for-item}")
	private String ratingUrl;
	
	@Value("${compare.products.paths.scraper.reviews}")
	private String reviewsUrl;
	
	
	
	
}
