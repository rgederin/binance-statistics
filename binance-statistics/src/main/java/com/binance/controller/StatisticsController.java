package com.binance.controller;

import com.binance.model.dto.SymbolStatisticDto;
import com.binance.model.dto.SymbolsDto;
import com.binance.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class StatisticsController {
    private StatisticsService statisticsService;


    /**
     * Simple health endpoint
     *
     * @return OK if server is up and running
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    /**
     * Symbols GET endpoint.
     *
     * @return List of symbols from the binance (only symbols code
     */
    @GetMapping("/symbols")
    public SymbolsDto getSymbols() {
        return statisticsService.getBinanceSymbols();
    }

    /**
     * Return current statistics for provided symbol
     *
     * @param symbol - binance symbol to get statistics
     * @return - object with statistics about symbol
     */
    @GetMapping("/{symbol}")
    public SymbolStatisticDto getSymbolStatistics(@PathVariable String symbol) {
        return statisticsService.getBinanceSymbolStatistic(symbol);
    }
}
