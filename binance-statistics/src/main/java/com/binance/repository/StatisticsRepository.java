package com.binance.repository;

import com.binance.model.SymbolStatistic;
import com.binance.model.generated.Symbols;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
@AllArgsConstructor
public class StatisticsRepository {
    private RestTemplate restTemplate;


    public Symbols getBinanceSymbols(){
        ResponseEntity<Symbols> response
                = restTemplate.getForEntity("https://api.binance.com/api/v3/exchangeInfo" , Symbols.class);

        return response.getBody();
    }
}
