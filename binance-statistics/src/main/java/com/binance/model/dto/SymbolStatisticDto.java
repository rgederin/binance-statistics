package com.binance.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymbolStatisticDto {
    private String symbol;
    private long appearance;
    private double priceMedian;
    private double lastPrice;
}
