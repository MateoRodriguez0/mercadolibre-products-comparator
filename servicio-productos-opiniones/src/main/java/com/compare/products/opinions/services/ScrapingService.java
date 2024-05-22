package com.compare.products.opinions.services;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope("prototype")
public class ScrapingService {

	public Double getRatingAverage(String parentId) throws IOException {
		Document document =null;
		document = Jsoup.connect(reviewsUrl.replace("{parent_id}",parentId))
					.get();
		return Double.parseDouble(document.getElementsByClass(calificationTag).text());
		
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
