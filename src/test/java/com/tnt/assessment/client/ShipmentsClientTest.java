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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShipmentsClientTest {

    private ShipmentsClient shipmentsClient;

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
        shipmentsClient = new ShipmentsClient(WebClient.create(baseUrl));
    }

    @Test
    void getShipments() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"1234567\": [\"aShipment\"]}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> shipmentsMono = shipmentsClient.getShipments("1234567");

        StepVerifier.create(shipmentsMono)
                .expectNextMatches(shipments -> {
                    HashMap<String, List<String>> response = (HashMap) shipments;
                    return response.get("1234567").get(0).equals("aShipment");
                })
                .verifyComplete();
    }

    @Test
    void getShipments400() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> shipmentsMono = shipmentsClient.getShipments("1234567");

        StepVerifier.create(shipmentsMono)
                .expectNextMatches(shipments -> shipments == "Error response")
                .verifyComplete();
    }

    @Test
    void getShipmentsOther() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{}")
                .addHeader("Content-Type", "application/json"));

        Mono<Object> shipmentsMono = shipmentsClient.getShipments("1234567");

        StepVerifier.create(shipmentsMono)
                .expectError()
                .verify();
    }
}