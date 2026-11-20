
package school.sptech.KentoCafe.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheConfiguration configure() {

        // Define quais classes podem ser desserializadas
        var validador = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("school.sptech.")
                .allowIfSubType("java.util.")
                .allowIfSubType("java.math.")
                .build();

        // Cria um ObjectMapper exclusivo para o Redis
        var objectMapper = new ObjectMapper()
                .findAndRegisterModules();

        // Habilita a tipagem polimórfica
        objectMapper.activateDefaultTyping(
                validador,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // Configura o serializador JSON
        var serializador =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(serializador)
                );
    }
}
