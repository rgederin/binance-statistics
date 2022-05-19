package com.binance.model.gson;


import lombok.Data;
import lombok.ToString;


@ToString
public class TradeStreamEntity {
    private Data data;

    public Data getData(){
        return this.data;
    }
    public class Data {
        private String s;
        private String p;

        public String getSymbol(){
            return this.s;
        }

        public String getPrice(){
            return this.p;
        }
    }
}
