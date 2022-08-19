package com.tnt.assessment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class AggregationDto {
    private HashMap<String, Double> pricing;
    private HashMap<String, String> track;
    private HashMap<String, List<String>> shipments;
}
