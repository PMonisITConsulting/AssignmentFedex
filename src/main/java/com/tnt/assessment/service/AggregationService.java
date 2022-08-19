package com.tnt.assessment.service;

import com.tnt.assessment.dto.AggregationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
public class AggregationService {

    @Autowired
    ShippingService service;

    private FluxSink<Pair<String, CompletableFuture<String>>> innerSink;

    public AggregationService() {
        Flux<Pair<String, CompletableFuture<String>>> queue = Flux.create(sink -> {
            innerSink = sink;
        });

        queue.bufferTimeout(3, Duration.ofSeconds(20)).map( results -> {
            for (Pair<String, CompletableFuture<String>> pair : results) {
                pair.getSecond().complete(pair.getFirst().toUpperCase(Locale.ROOT));
            }

            System.out.println("Received number of lists" + results.size());

            return "ok";
        }).subscribe();
    }

    public Mono<String> request(String requests) {
        CompletableFuture<String> future = new CompletableFuture<>();

        innerSink.next(Pair.of(requests, future));

        return Mono.fromFuture(future);
    }

//    public Flux<AggregationDto> aggregate(List<String> shipping) {
//        return Flux.zip(service.request(shipping), service.request(shipping))
//                .map( result -> {
//                    AggregationDto aggregation = new AggregationDto();
//
//                    HashMap<String, String[]> example = new HashMap<>();
//
//                    String[] aString = new String[1];
//
//                    aString[0] = result.getT1();
//
//                    example.put("aKey", aString);
//                    aggregation.setShipments(example);
//
//                    return aggregation;
//                });
//    }
}
