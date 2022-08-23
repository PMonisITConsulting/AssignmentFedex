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
import java.util.List;

@SpringBootTest
class TrackClientTest {

    private TrackClient trackClient;

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
        trackClient = new TrackClient(WebClient.create(baseUrl));
    }

    @Test
    void getTracking() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"1234567\": \"aTracking\"}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> trackingsMono = trackClient.getTracking("1234567");

        StepVerifier.create(trackingsMono)
                .expectNextMatches(tracking -> {
                    HashMap<String, String> response = (HashMap) tracking;
                    return response.get("1234567").equals("aTracking");
                })
                .verifyComplete();
    }

    @Test
    void getTracking400() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> trackingsMono = trackClient.getTracking("1234567");

        StepVerifier.create(trackingsMono)
                .expectNextMatches(tracking -> tracking == "Error response")
                .verifyComplete();
    }

    @Test
    void getTrackingOther() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> trackingsMono = trackClient.getTracking("1234567");

        StepVerifier.create(trackingsMono)
                .expectError()
                .verify();
    }
}