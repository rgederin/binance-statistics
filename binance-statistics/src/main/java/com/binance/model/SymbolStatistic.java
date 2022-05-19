package com.binance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.PriorityQueue;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SymbolStatistic {
    private String symbol;
    private long appearance;
    private double priceMedian;
    private double lastPrice;
    private PriorityQueue<Double> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    private PriorityQueue<Double> minHeap = new PriorityQueue<>();
}
