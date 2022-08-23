package com.tnt.assessment.service;

import com.tnt.assessment.client.PricingClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
class PricingServiceTest {

    @Autowired
    private PricingService service;

    @MockBean
    private PricingClient client;

    @Test
    void requestCommaLimitedLessThan5() {
        long startTime = System.nanoTime();
        Map<String, Double> resultMap  = new HashMap<>() {{
            put("NL", 123d);
            put("PT", 456d);
        }};

        when(client.getPricing("NL,PT")).thenReturn(Mono.just(resultMap));
        Mono<HashMap<String, Double>> resultMono = service.request(Arrays.asList("NL", "PT"));

        resultMono.map( result -> {
            assertEquals(123d, result.get("NL"));
            assertEquals(456d, result.get("PT"));
            assertTrue(((System.nanoTime() - startTime) / 1000000000) >= 5);

            return "ok";
        }).block();
    }

    @Test
    void requestCommaLimitedMoreOrEqualThan5() {
        long startTime = System.nanoTime();
        Map<String, Double> resultMap  = new HashMap<>() {{
            put("NL", 123d);
        }};

        when(client.getPricing(anyString())).thenReturn(Mono.just(resultMap));
        Mono<HashMap<String, Double>> resultMono = service.request(Arrays.asList("NL"));
        service.request(Arrays.asList("NL"));
        service.request(Arrays.asList("NL"));
        service.request(Arrays.asList("NL"));
        service.request(Arrays.asList("NL"));


        resultMono.map( result -> {
            assertEquals(123d, result.get("NL"));
            assertTrue(((System.nanoTime() - startTime) / 1000000000) < 5);

            return "ok";
        }).block();
    }
}