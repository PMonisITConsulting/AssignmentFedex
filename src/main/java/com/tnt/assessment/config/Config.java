package com.tnt.assessment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Value("${backEndServices.baseUri}")
    String baseUri;

    @Bean
    public WebClient shipmentsApiClient() {
        return WebClient.create(baseUri);
    }

}
