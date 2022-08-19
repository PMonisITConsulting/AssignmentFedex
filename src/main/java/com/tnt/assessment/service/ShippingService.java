package com.tnt.assessment.service;

import com.tnt.assessment.client.ShipmentsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class ShippingService {

    @Autowired
    ShipmentsClient client;

    public ShippingService(Flux<List<String>> queue) {
        this.queue = queue;
    }

    private Flux<List<String>> queue = Flux.empty();

    public Flux<String> request(List<String> requests) {

        queue = Flux.concat(queue, Flux.just(requests));

        //List<List<String>> tryout = queue.bufferTimeout(5, Duration.ofSeconds(100)).blockLast(Duration.ofSeconds(100));

        //System.out.println(tryout);
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();

        List<String> queue = new ArrayList<>();

        return null;

//        Flux<String> flux = Flux.generate(() -> 0, queue, sink -> {
//            if (queue.size() == 5) {
//
//            }
//        })
//
//
//        return queue.bufferTimeout(5, Duration.ofSeconds(100)).map( results -> {
//            HashMap<String, String[]> response = client.getShipments().map( ship -> ship).block();
//
//            return Arrays.stream(response.get("109347263")).findFirst().get();
//        });
    }
}
