package com.michaelcanonizado.backend.configurations;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;


@Configuration(proxyBeanMethods = false)
public class RedisConfiguration {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        /* List of allowed classes to be stored in cache */
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.michaelcanonizado.backend.dtos")
                .allowIfSubType("com.michaelcanonizado.backend.models")
                .allowIfSubType("com.fasterxml.jackson.databind.node")
                .allowIfSubType("java.time")
                .allowIfSubType("java.util")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        /* Jackson support for Java 8 Data and Time */
        mapper.registerModule(new JavaTimeModule());
        /* Write dates as ISO strings not as epoch time */
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.activateDefaultTyping(
                ptv,
                /* Tried to patch this but couldn't find a solution. For now using EVERYTHING
                   is fine as the system is closed and only runs locally. Using the proposed
                   alternative: NON_FINAL_AND_ENUMS doesn't work with records. Possible reason:
                   https://github.com/FasterXML/jackson-databind/issues/3356 */
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.WRAPPER_ARRAY
        );

        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(mapper, Object.class);

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(serializer)
                )
                /* Adjust TTL accordingly */
                .entryTtl(Duration.ofHours(6))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}

