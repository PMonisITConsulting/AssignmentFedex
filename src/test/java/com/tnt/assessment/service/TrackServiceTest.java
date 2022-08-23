package com.tnt.assessment.service;

import com.tnt.assessment.client.ShipmentsClient;
import com.tnt.assessment.client.TrackClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrackServiceTest {

    @Autowired
    private TrackService service;

    @MockBean
    private TrackClient client;

    @Test
    void requestCommaLimitedLessThan5() {
        long startTime = System.nanoTime();
        Map<String, String> resultMap  = new HashMap<>() {{
            put("123456", "aTracking");
            put("654321", "anotherTracking");
        }};

        when(client.getTracking("123456,654321")).thenReturn(Mono.just(resultMap));
        Mono<HashMap<String, String>> resultMono = service.request(Arrays.asList("123456", "654321"));

        resultMono.map( result -> {
            assertEquals("aTracking", result.get("123456"));
            assertEquals("anotherTracking", result.get("654321"));
            assertTrue(((System.nanoTime() - startTime) / 1000000000) >= 5);

            return "ok";
        }).block();
    }

    @Test
    void requestCommaLimitedMoreOrEqualThan5() {
        long startTime = System.nanoTime();
        Map<String, String> resultMap  = new HashMap<>() {{
            put("123456", "aTracking");
        }};

        when(client.getTracking(anyString())).thenReturn(Mono.just(resultMap));
        Mono<HashMap<String, String>> resultMono = service.request(Arrays.asList("123456"));
        service.request(Arrays.asList("123456"));
        service.request(Arrays.asList("123456"));
        service.request(Arrays.asList("123456"));
        service.request(Arrays.asList("123456"));


        resultMono.map( result -> {
            assertEquals("aTracking", result.get("123456"));
            assertTrue(((System.nanoTime() - startTime) / 1000000000) < 5);

            return "ok";
        }).block();
    }
}