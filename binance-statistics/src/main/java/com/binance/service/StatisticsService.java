package com.binance.service;

import com.binance.StatisticsCalculator;
import com.binance.model.SymbolStatistic;
import com.binance.model.dto.SymbolsDto;
import com.binance.model.generated.Symbol;
import com.binance.model.generated.Symbols;
import com.binance.repository.StatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsService {
    private StatisticsRepository statisticsRepository;

    public SymbolsDto getBinanceSymbols(){
        SymbolsDto symbolsDto = new SymbolsDto();

        symbolsDto.setBinanceSymbols(statisticsRepository.getBinanceSymbols()
                .getSymbols()
                .stream()
                .map(Symbol::getSymbol)
                .collect(Collectors.toList()));

        return symbolsDto;
    }

    public SymbolStatistic getBinanceSymbolStatistic(String symbol) {
        return StatisticsCalculator.statisticsHashMap.getOrDefault(symbol.toUpperCase(), null);
    }
}
