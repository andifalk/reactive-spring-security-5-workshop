package com.example.library.server.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    /*
     * Helper to map from entity beans to value objects and the other way round.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
