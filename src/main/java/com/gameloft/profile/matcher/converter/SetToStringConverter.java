package com.gameloft.profile.matcher.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Set;

@Converter
public class SetToStringConverter implements AttributeConverter<Set<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert Set to String", e);
        }
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : objectMapper.readValue(dbData, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert String to Set", e);
        }
    }
}