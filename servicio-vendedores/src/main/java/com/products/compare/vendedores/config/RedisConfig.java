package com.products.compare.vendedores.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class RedisConfig {

	@Bean
	RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
			    .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
			    .transactionAware()
			    .withCacheConfiguration("InfoSellerPublicationCache",
			    		RedisCacheConfiguration.defaultCacheConfig()
				        .entryTtl(Duration.ofMinutes(45))
				        .serializeValuesWith(SerializationPair.fromSerializer(
				        		new GenericJackson2JsonRedisSerializer())))
			    .withCacheConfiguration("InfoSellerCache", RedisCacheConfiguration.defaultCacheConfig()
				        .entryTtl(Duration.ofMinutes(30))
				        .serializeValuesWith(SerializationPair.fromSerializer(
				        		new GenericJackson2JsonRedisSerializer())))
			    .withCacheConfiguration("InfoStateCache", RedisCacheConfiguration.defaultCacheConfig()
				        .entryTtl(Duration.ofHours(1))
				        .serializeValuesWith(SerializationPair.fromSerializer(
				        		new GenericJackson2JsonRedisSerializer())))
			    .build();
				
		return cacheManager;
	}
	
}
