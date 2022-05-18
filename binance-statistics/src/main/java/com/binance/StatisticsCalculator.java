package com.binance;

import com.binance.model.SymbolStatistic;
import com.binance.model.gson.TradeStreamEntity;

import java.util.HashMap;
import java.util.PriorityQueue;

public class StatisticsCalculator {
    public static final HashMap<String, SymbolStatistic> statisticsHashMap = new HashMap<>();


    public static void addStatistics(TradeStreamEntity tradeStreamEntity){
        if (statisticsHashMap.containsKey(tradeStreamEntity.getData().s)){
            SymbolStatistic existed = statisticsHashMap.get(tradeStreamEntity.getData().s);

            SymbolStatistic current = new SymbolStatistic();
            current.setAppearance(existed.getAppearance() + 1);
            current.setLastPrice(Float.parseFloat(tradeStreamEntity.getData().p));
            current.setSymbol(existed.getSymbol());

            addToPriorityQueue(Float.parseFloat(tradeStreamEntity.getData().p), current.getMax(), current.getMin());
            current.setPriceMedian(findMedian(current.getMax(), current.getMin()));

            statisticsHashMap.put(existed.getSymbol(), current);
        } else {
            SymbolStatistic symbol = new SymbolStatistic();

            symbol.setSymbol(tradeStreamEntity.getData().s);
            symbol.setAppearance(1);
            symbol.setLastPrice(Float.parseFloat(tradeStreamEntity.getData().p));

            addToPriorityQueue(Float.parseFloat(tradeStreamEntity.getData().p), symbol.getMax(), symbol.getMin());
            symbol.setPriceMedian(findMedian(symbol.getMax(), symbol.getMin()));
            statisticsHashMap.put(symbol.getSymbol(), symbol);
        }
    }

    private static void addToPriorityQueue(float x, PriorityQueue<Float> max, PriorityQueue<Float> min) {
        if(max.isEmpty() || max.peek() >= x){
            max.add(x);
        }else{
            min.add(x);
        }

        if(max.size() - min.size() == 2){
            min.add(max.poll());
        }else if(min.size() - max.size() == 2){
            max.add(min.poll());
        }
    }

    public static double findMedian(PriorityQueue<Float> max, PriorityQueue<Float> min) {
        if(max.size() == min.size()){
            return (double)(max.peek() + min.peek())/2;

        } else if(min.size()<max.size())
            return (double)(max.peek());
        else
            return (double)(min.peek());

    }

}
