package com.products.compare.url.analisis.config;

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
			    .withCacheConfiguration("ItemDetailsCache",   RedisCacheConfiguration.defaultCacheConfig()
				        .entryTtl(Duration.ofMinutes(45))
				        .serializeValuesWith(SerializationPair.fromSerializer(
				        		new GenericJackson2JsonRedisSerializer())))
			    .build();
				
		return cacheManager;
	}
	
}
