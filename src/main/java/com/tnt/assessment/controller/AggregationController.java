package com.tnt.assessment.controller;

import com.tnt.assessment.client.PricingClient;
import com.tnt.assessment.client.ShipmentsClient;
import com.tnt.assessment.client.TrackClient;
import com.tnt.assessment.dto.AggregationDto;
import com.tnt.assessment.service.AggregationService;
import com.tnt.assessment.service.PricingService;
import com.tnt.assessment.service.ShipmentsService;
import com.tnt.assessment.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aggregation")
public class AggregationController {

    @Autowired
    ShipmentsService shipmentsService;
    @Autowired
    PricingService pricingService;
    @Autowired
    TrackService trackService;

    @GetMapping
    public AggregationDto getAggregation(@RequestParam List<String> pricing,
                                         @RequestParam List<String> track,
                                         @RequestParam List<String> shipments) {
        AggregationDto aggregation = new AggregationDto();

        Mono<Tuple3<HashMap<String, String[]>, HashMap<String, Double>, HashMap<String, String>>> results = Mono.zip(
                shipmentsService.request(shipments),
                pricingService.request(pricing),
                trackService.request(track)
        );

        results.map(tuple -> {
            aggregation.setShipments((HashMap<String, String[]>) tuple.get(0));
            aggregation.setPricing((HashMap<String, Double>) tuple.get(1));
            aggregation.setTrack((HashMap<String, String>) tuple.get(2));

            return "ok";
        }).block();

        return aggregation;
    }
}
