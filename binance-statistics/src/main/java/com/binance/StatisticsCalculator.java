package com.binance;

import com.binance.model.SymbolStatistic;
import com.binance.model.gson.TradeStreamEntity;

import java.util.HashMap;

public class StatisticsCalculator {
    public static final HashMap<String, SymbolStatistic> statisticsHashMap = new HashMap<>();


    public static void addStatistics(TradeStreamEntity tradeStreamEntity){
        if (statisticsHashMap.containsKey(tradeStreamEntity.getData().s)){
            SymbolStatistic existed = statisticsHashMap.get(tradeStreamEntity.getData().s);

            SymbolStatistic current = new SymbolStatistic();
            current.setAppearance(existed.getAppearance() + 1);
            current.setLastPrice(Float.parseFloat(tradeStreamEntity.getData().p));
            current.setSymbol(existed.getSymbol());

            statisticsHashMap.put(existed.getSymbol(), current);
        } else {
            SymbolStatistic symbol = new SymbolStatistic();

            symbol.setSymbol(tradeStreamEntity.getData().s);
            symbol.setAppearance(1);
            symbol.setLastPrice(Float.parseFloat(tradeStreamEntity.getData().p));

            statisticsHashMap.put(symbol.getSymbol(), symbol);
        }
    }

}
