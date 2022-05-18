package com.binance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.PriorityQueue;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SymbolStatistic {
    private String symbol;
    private long appearance;
    private double priceMedian;
    private float lastPrice;
    private PriorityQueue<Float> min = new PriorityQueue<>();
    private PriorityQueue<Float> max = new PriorityQueue<>();
}
