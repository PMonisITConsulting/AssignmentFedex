package com.tnt.assessment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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

    public Mono<HashMap<String, String[]>> getShipments() {
        return client
                .get()
                .uri("shipments?q=109347263,123456891")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
