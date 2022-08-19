package com.tnt.assessment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShipmentsClient {

    private final WebClient client;

    public Mono<Object> getShipments(String shipments) {
        return client
                .get()
                .uri("shipments?q=" + shipments)
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
