package com.binance.controller;

import com.binance.model.dto.SymbolsDto;
import com.binance.service.StatisticsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
public class StatisticsController {
    private StatisticsService statisticsService;

    @GetMapping("/health")
    public String health(){
        return "OK";
    }

    @GetMapping("/symbols")
    public SymbolsDto getSymbols(){
        return statisticsService.getBinanceSymbols();
    }
}
