package com.gameloft.profile.matcher.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(OffsetDateTime.class, new CustomOffsetDateTimeDeserializer());
        module.addSerializer(OffsetDateTime.class, new CustomOffsetDateTimeSerializer());

        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
