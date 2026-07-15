package com.peplatform.portfolioservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Registers modules so Jackson can read/write Java 8 Dates like LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}