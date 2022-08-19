package com.tnt.assessment.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PricingClient {

    private final WebClient client;

    public Mono<Object> getPricing(String countries) {
        return client
                .get()
                .uri("pricing?q=" + countries)
                .exchangeToMono( response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(new ParameterizedTypeReference<>() {});
                    } else if (response.statusCode().is4xxClientError()) {
                        return Mono.just("Error response");
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                });
    }
}
