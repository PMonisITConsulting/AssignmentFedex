package com.tnt.assessment.service;

import com.google.common.collect.Lists;
import com.tnt.assessment.client.PricingClient;
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
public class PricingService {

    @Value("${apiSettings.callCap}")
    private Integer callCap;
    @Value("${apiSettings.commaDelimitedValues}")
    private Integer commaDelimitedValues;

    @Autowired
    private PricingClient client;

    private FluxSink<Pair<List<String>, CompletableFuture<HashMap<String, Double>>>> innerSink;

    public PricingService() {
        Flux<Pair<List<String>, CompletableFuture<HashMap<String, Double>>>> queue =
                Flux.create(sink -> innerSink = sink);

        queue.bufferTimeout(callCap, Duration.ofSeconds(20)).map(requestParams -> {
            List<String> requestParamList = new ArrayList<>();

            requestParams.forEach(param -> requestParamList.addAll(param.getFirst()));

            HashMap<String, Double> responses = new HashMap<>();

            Flux.fromIterable(Lists.partition(requestParamList, commaDelimitedValues))
                    .flatMap(parameters -> client.getPricing(String.join(",", parameters)))
                    .collectMap(response -> {
                        responses.putAll((HashMap) response);

                        return true;
                    })
                    .doOnSuccess(p -> {
                        for (Pair<List<String>, CompletableFuture<HashMap<String, Double>>> rp : requestParams) {
                            HashMap<String, Double> futureResponse = new HashMap<>();

                            rp.getFirst().forEach(id -> futureResponse.put(id, responses.get(id)));
                            rp.getSecond().complete(futureResponse);
                        }
                    }).subscribe();

            return "ok";
        }).subscribe();
    }

    public Mono<HashMap<String, Double>> request(List<String> requests) {
        CompletableFuture<HashMap<String, Double>> future = new CompletableFuture<>();

        innerSink.next(Pair.of(requests, future));

        return Mono.fromFuture(future);
    }
}
