package com.binance.service;

import com.binance.model.SymbolStatistic;
import com.binance.model.dto.SymbolsDto;
import com.binance.model.generated.Symbol;
import com.binance.model.generated.Symbols;
import com.binance.model.gson.TradeStreamEntity;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsService {
    private final RestTemplate restTemplate;

    private final MedianCalculatorService medianCalculatorService;

    private final Map<String, SymbolStatistic> statisticsHashMap = new ConcurrentHashMap<>();

    public SymbolsDto getBinanceSymbols(){
        ResponseEntity<Symbols> response
                = restTemplate.getForEntity("https://api.binance.com/api/v3/exchangeInfo" , Symbols.class);

        SymbolsDto symbolsDto = new SymbolsDto();

        symbolsDto.setBinanceSymbols(Objects.requireNonNull(response.getBody())
                .getSymbols()
                .stream()
                .map(Symbol::getSymbol)
                .collect(Collectors.toList()));

        return symbolsDto;
    }

    public SymbolStatistic getBinanceSymbolStatistic(String symbol) {
        return statisticsHashMap.getOrDefault(symbol.toUpperCase(), null);
    }

    public void updateSymbolsStatistics(TradeStreamEntity tradeStreamEntity){
        String symbolToUpdate = tradeStreamEntity.getData().getSymbol();
        double symbolLastPrice = Double.parseDouble(tradeStreamEntity.getData().getPrice());

        if (statisticsHashMap.containsKey(symbolToUpdate)){
            SymbolStatistic existedStatistics = statisticsHashMap.get(symbolToUpdate);

            SymbolStatistic updatedStatistics = new SymbolStatistic();

            updatedStatistics.setAppearance(existedStatistics.getAppearance() + 1);
            updatedStatistics.setLastPrice(symbolLastPrice);
            updatedStatistics.setSymbol(symbolToUpdate);

            medianCalculatorService.addToPriorityQueue(symbolLastPrice, existedStatistics.getMaxHeap(), existedStatistics.getMinHeap());
            updatedStatistics.setPriceMedian(medianCalculatorService.findMedian(existedStatistics.getMaxHeap(), existedStatistics.getMinHeap()));

            updatedStatistics.setMaxHeap(existedStatistics.getMaxHeap());
            updatedStatistics.setMinHeap(existedStatistics.getMinHeap());

            statisticsHashMap.put(symbolToUpdate, updatedStatistics);
        } else {
            SymbolStatistic updatedStatistics = new SymbolStatistic();

            updatedStatistics.setSymbol(symbolToUpdate);
            updatedStatistics.setAppearance(1);
            updatedStatistics.setLastPrice(symbolLastPrice);

            medianCalculatorService.addToPriorityQueue(symbolLastPrice, updatedStatistics.getMaxHeap(), updatedStatistics.getMinHeap());
            updatedStatistics.setPriceMedian(medianCalculatorService.findMedian(updatedStatistics.getMaxHeap(), updatedStatistics.getMinHeap()));

            statisticsHashMap.put(symbolToUpdate, updatedStatistics);
        }
    }
}
