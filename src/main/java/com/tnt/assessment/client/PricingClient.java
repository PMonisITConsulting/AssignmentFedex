package com.tnt.assessment.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PricingClient {

    private final WebClient client;

    public Mono<HashMap<String, Float>> getPricing() {
        return client
                .get()
                .uri("pricing?q=NL,CN")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
