package com.binance.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class SymbolsDto {
    private List<String> binanceSymbols;
}
