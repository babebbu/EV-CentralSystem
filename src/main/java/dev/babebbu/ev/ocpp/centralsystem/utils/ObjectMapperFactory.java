package dev.babebbu.ev.ocpp.centralsystem.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperFactory {

    private static final ObjectMapper mapper = new ObjectMapper()
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

}
