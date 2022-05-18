package com.binance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SymbolStatistic {
    private String symbol;
    private long appearance;
    private double priceMedian;
    private float lastPrice;
}
