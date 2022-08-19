package com.tnt.assessment.service;

import com.google.common.collect.Lists;
import com.tnt.assessment.client.ShipmentsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentsClient client;

    private FluxSink<Pair<List<String>, CompletableFuture<HashMap<String, List<String>>>>> innerSink;

    public ShipmentService(@Value("${apiSettings.callCap}") Integer callCap,
                           @Value("${apiSettings.timeCap}") Integer timeCap,
                           @Value("${apiSettings.commaDelimitedCap}") Integer commaDelimitedCap) {
        Flux<Pair<List<String>, CompletableFuture<HashMap<String, List<String>>>>> queue =
                Flux.create(sink -> innerSink = sink);

        queue.bufferTimeout(callCap, Duration.ofSeconds(timeCap)).map(requestParams -> {
            List<String> requestParamList = new ArrayList<>();

            requestParams.forEach(param -> requestParamList.addAll(param.getFirst()));

            HashMap<String, List<String>> responses = new HashMap<>();

            Flux.fromIterable(Lists.partition(requestParamList, commaDelimitedCap))
                    .flatMap(parameters -> client.getShipments(String.join(",", parameters)))
                    .collectMap(response -> {
                        responses.putAll((HashMap) response);

                        return true;
                    })
                    .doOnSuccess(p -> {
                        for (Pair<List<String>, CompletableFuture<HashMap<String, List<String>>>> rp : requestParams) {
                            HashMap<String, List<String>> futureResponse = new HashMap<>();

                            rp.getFirst().forEach(id -> futureResponse.put(id, responses.get(id)));
                            rp.getSecond().complete(futureResponse);
                        }
                    }).subscribe();

            return "ok";
        }).subscribe();
    }

    private String[] arrayListToArray(List<String> list) {
        return (String[]) list.toArray();
    }

    public Mono<HashMap<String, List<String>>> request(List<String> requests) {
        CompletableFuture<HashMap<String, List<String>>> future = new CompletableFuture<>();

        innerSink.next(Pair.of(requests, future));

        return Mono.fromFuture(future);
    }
}
