package com.tnt.assessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
public class AggregationService {

//    @Autowired
//    ShipmentsService service;
//
//    private FluxSink<Pair<List<String>, CompletableFuture<String>>> innerSink;
//
//    public AggregationService() {
//        Flux<Pair<List<String>, CompletableFuture<String>>> queue = Flux.create(sink -> {
//            innerSink = sink;
//        });
//
//        queue.bufferTimeout(3, Duration.ofSeconds(20)).map( results -> {
//            for (Pair<List<String>, CompletableFuture<String>> pair : results) {
//                pair.getSecond().complete(pair.getFirst().toUpperCase(Locale.ROOT));
//            }
//
//            System.out.println("Received number of lists" + results.size());
//
//            return "ok";
//        }).subscribe();
//    }
//
//    public Mono<List<String>> request(List<String> requests) {
//        CompletableFuture<String> future = new CompletableFuture<>();
//
//        innerSink.next(Pair.of(requests, future));
//
//        return Mono.fromFuture(future);
//    }
}
