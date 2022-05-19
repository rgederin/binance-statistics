package com.binance.service;

import com.binance.map.CustomHashMap;
import com.binance.model.SymbolStatistic;
import com.binance.model.dto.SymbolStatisticDto;
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
    private static final String BINANCE_SYMBOLS_API_URL = "https://api.binance.com/api/v3/exchangeInfo";
    private final RestTemplate restTemplate;

    private final MedianCalculatorService medianCalculatorService;

    private final CustomHashMap statisticsHashMap = new CustomHashMap();


    /**
     * Invoke binance API for getting all supported symbols.
     * Using simple REST template for this
     * TODO: add graceful exception handling
     *
     * @return DTO which will be returning by the corresponding controller
     */
    public SymbolsDto getBinanceSymbols() {
        ResponseEntity<Symbols> response = restTemplate.getForEntity(BINANCE_SYMBOLS_API_URL, Symbols.class);

        SymbolsDto symbolsDto = new SymbolsDto();

        symbolsDto.setBinanceSymbols(Objects.requireNonNull(response.getBody()).getSymbols().stream().map(Symbol::getSymbol).collect(Collectors.toList()));

        return symbolsDto;
    }

    public SymbolStatisticDto getBinanceSymbolStatistic(String symbol) {
        SymbolStatistic symbolStatistic = statisticsHashMap.get(symbol.toUpperCase());

        if (Objects.isNull(symbolStatistic)) {
            return null;
        }

        return SymbolStatisticDto.builder().symbol(symbolStatistic.getSymbol()).appearance(symbolStatistic.getAppearance()).lastPrice(symbolStatistic.getLastPrice()).priceMedian(symbolStatistic.getPriceMedian()).build();
    }

    /**
     * Update statistics entry in the custom hash map
     *
     * @param tradeStreamEntity - event from the web socket
     */
    public void updateSymbolsStatistics(TradeStreamEntity tradeStreamEntity) {
        String symbolToUpdate = tradeStreamEntity.getData().getSymbol();
        double symbolLastPrice = Double.parseDouble(tradeStreamEntity.getData().getPrice());

        // if entry already exists in the hash map
        if (statisticsHashMap.get(symbolToUpdate) != null) {
            //fetch existed value
            SymbolStatistic existedStatistics = statisticsHashMap.get(symbolToUpdate);

            //create new value for given symbol, increase appearance and recalculate median
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
            // such symbol not existed in the map, create new one
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
