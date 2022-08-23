package com.tnt.assessment.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PricingClientTest {

    private PricingClient pricingClient;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        pricingClient = new PricingClient(WebClient.create(baseUrl));
    }

    @Test
    void getPricing() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"NL\": 14.67}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> pricingMono = pricingClient.getPricing("NL");

        StepVerifier.create(pricingMono)
                .expectNextMatches(pricing -> {
                    HashMap<String, Double> response = (HashMap) pricing;
                    return response.get("NL") == 14.67d;
                })
                .verifyComplete();
    }

    @Test
    void getPricing400() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> pricingMono = pricingClient.getPricing("NL");

        StepVerifier.create(pricingMono)
                .expectNextMatches(pricing -> pricing == "Error response")
                .verifyComplete();
    }

    @Test
    void getPricingOther() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> pricingMono = pricingClient.getPricing("NL");

        StepVerifier.create(pricingMono)
                .expectError()
                .verify();
    }
}