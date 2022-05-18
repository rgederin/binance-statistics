package com.binance.model.gson;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TradeStreamEntity {
    public Data data;

    public class Data {
        public String s;
        public String p;
    }
}
