package com.tnt.assessment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class AggregationDto {
    private HashMap<String, Float> pricing;
    private HashMap<String, String> track;
    private HashMap<String, String[]> shipments;
}
