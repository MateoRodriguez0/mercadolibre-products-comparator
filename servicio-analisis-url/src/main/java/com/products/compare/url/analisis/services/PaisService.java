package com.products.compare.url.analisis.services;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.products.compare.url.analisis.clients.MercadoLibreSitesClient;

@Service
public class PaisService {

	public List<String> guardarPaisesEnCache(List<JsonNode> paises) {
		List<String> paisesCast = paises.parallelStream()
				.map(j -> j.get("id").asText())
				.collect(Collectors.toList());

		redisTemplate.opsForValue().set("paises", paisesCast, 30, TimeUnit.DAYS);
		return paisesCast;
	}

	@SuppressWarnings("unchecked")
	public List<String> getPaises() {
		List<String> paises = (List<String>) redisTemplate.opsForValue().get("paises");
		if (paises == null) {
			paises = guardarPaisesEnCache(client.getPaises());

		}
		return paises;
	}

	@Scheduled(fixedRate = 30, timeUnit = TimeUnit.DAYS) // 24 horas
	public void actualizarCachePaises() {
		List<JsonNode> paises = client.getPaises();
		guardarPaisesEnCache(paises);
	}

	@Autowired
	private MercadoLibreSitesClient client;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
}
