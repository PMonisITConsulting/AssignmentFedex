package com.tnt.assessment.controller;

import com.tnt.assessment.client.PricingClient;
import com.tnt.assessment.client.ShipmentsClient;
import com.tnt.assessment.client.TrackClient;
import com.tnt.assessment.dto.AggregationDto;
import com.tnt.assessment.service.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aggregation")
public class AggregationController {

    @Autowired
    ShipmentsClient shipmentsClient;
    @Autowired
    TrackClient trackClient;
    @Autowired
    PricingClient pricingClient;

    @Autowired
    AggregationService service;

    @GetMapping
    public Flux<AggregationDto> getAggregation(@RequestParam List<String> pricing,
                                         @RequestParam List<String> track,
                                         @RequestParam List<String> shipments) {
        AggregationDto aggregation = new AggregationDto();

//        Flux.fromArray(shipments.toArray()).bufferTimeout(5, Duration.ofSeconds(10))
//                .map(list -> System.out.println(list) );

        return null;

//        Flux.merge(Arrays.asList(shipmentsClient.getShipments(), pricingClient.getPricing(), trackClient.getTrack()))
//                .bufferTimeout(5, Duration.ofSeconds(10))
//                .doOnNext(responses -> {
//                    aggregation.setShipments((HashMap<String, String[]>) responses.get(0));
//                    aggregation.setPricing((HashMap<String, Float>) responses.get(1));
//                    aggregation.setTrack((HashMap<String, String>) responses.get(2));
//                })
//                .then()
//                .block();
    }

//    private void subscriberTest() {
//        t.bufferTimeout(5, Duration.ofSeconds(20)).doOnNext(responses -> {
//            aggregation.setShipments((HashMap<String, String[]>) responses.get(0));
//        });
//    }
}
