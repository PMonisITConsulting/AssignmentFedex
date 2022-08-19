package com.tnt.assessment.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TrackClient {

    private final WebClient client;

    public Mono<HashMap<String, String>> getTrack() {
        return client
                .get()
                .uri("track?q=109347263,123456891")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}
