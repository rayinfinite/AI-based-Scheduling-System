package com.github.rayinfinite.scheduler.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Defining the Default Cache Configuration
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()));

        // If there is a specific cache that requires a different expiration time, you can define the
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {


        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serialising a key with StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        // Use GenericJackson2JsonRedisSerializer to serialise the value.
        template.setValueSerializer(redisSerializer());

        // If you also intend to use hashed data structures (hashes), make sure that the serialiser for hashed keys and values is also set up
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(redisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    public RedisSerializer<Object> redisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.EVERYTHING, JsonTypeInfo.As.PROPERTY);

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
