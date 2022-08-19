package com.tnt.assessment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple5;

import static org.junit.jupiter.api.Assertions.*;

class AggregationServiceTest {

    private AggregationService service = new AggregationService();

    @BeforeEach
    void setUp() {
    }

    @Test
    public void aggregationTest() {
        Mono<Tuple5<String, String, String, String, String>> results = Mono.zip(
                service.request("a"),
                service.request("b"),
                service.request("c"),
                service.request("d"),
                service.request("e")
        );

        results.map(tuple -> {
            System.out.println(tuple.get(0));
            System.out.println(tuple.get(1));
            System.out.println(tuple.get(2));
            System.out.println(tuple.get(3));
            System.out.println(tuple.get(4));

            return "ok";
        }).block();
    }

    @Test
    public void aggregationTestTimeOut() {
        Mono<Tuple2<String, String>> results = Mono.zip(
                service.request("a"),
                service.request("b")
        );

        results.map(tuple -> {
            System.out.println(tuple.get(0));
            System.out.println(tuple.get(1));

            return "ok";
        }).block();
    }
}