package com.tnt.assessment.controller;

import com.tnt.assessment.service.PricingService;
import com.tnt.assessment.service.ShipmentService;
import com.tnt.assessment.service.TrackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class AggregationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ShipmentService shipmentService;
    @MockBean
    PricingService pricingService;
    @MockBean
    TrackService trackService;

    @Test
    void getAggregation() throws Exception {
        HashMap<String, List<String>> shipmentResponse = new HashMap<>() {{
            put("123456", Arrays.asList("aShipment"));
        }};
        HashMap<String, Double> pricingResponse = new HashMap<>() {{
            put("123456", 10d);
        }};
        HashMap<String, String> trackResponse = new HashMap<>() {{
            put("123456", "aTracking");
        }};

        when(shipmentService.request(any())).thenReturn(Mono.just(shipmentResponse));
        when(pricingService.request(any())).thenReturn(Mono.just(pricingResponse));
        when(trackService.request(any())).thenReturn(Mono.just(trackResponse));

        this.mockMvc
                .perform(get("/aggregation?pricing=NL&track=109347263&shipments=109347263"))
                .andExpect(content().string(
                        containsString("{\"pricing\":{\"123456\":10.0},\"track\":{\"123456\":\"aTracking\"},\"shipments\":{\"123456\":[\"aShipment\"]}}")));

    }
}