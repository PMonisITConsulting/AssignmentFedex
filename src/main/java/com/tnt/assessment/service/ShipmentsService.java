package com.tnt.assessment.service;

import com.tnt.assessment.client.ShipmentsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ShipmentsService {

    @Autowired
    ShipmentsClient client;

    private FluxSink<Pair<List<String>, CompletableFuture<HashMap<String, String[]>>>> innerSink;

    public ShipmentsService() {
        Flux<Pair<List<String>, CompletableFuture<HashMap<String, String[]>>>> queue =
                Flux.create(sink -> innerSink = sink);

        queue.bufferTimeout(1, Duration.ofSeconds(20)).map( results -> {
            List<String> concatenatedLists = new ArrayList<>();

            results.forEach( result -> concatenatedLists.addAll(result.getFirst()));

            HashMap<String, List<String>> response = (HashMap) client.getShipments(String.join(",", concatenatedLists)).block();

            for (Pair<List<String>, CompletableFuture<HashMap<String, String[]>>> pair : results) {
                HashMap<String, String[]> toReply = new HashMap<>();

                pair.getFirst().forEach( id -> toReply.put(id, Arrays.copyOf(response.get(id).toArray(), response.get(id).toArray().length, String[].class)));

                pair.getSecond().complete(toReply);
            }

            return "ok";
        }).subscribe();
    }

    public Mono<HashMap<String, String[]>> request(List<String> requests) {
        CompletableFuture<HashMap<String, String[]>> future = new CompletableFuture<>();

        innerSink.next(Pair.of(requests, future));

        return Mono.fromFuture(future);
    }
}
