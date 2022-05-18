package com.binance.websocket;

import com.binance.StatisticsCalculator;
import com.binance.model.gson.TradeStreamEntity;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.net.URI;


public class BinanceWebSocketClient extends WebSocketClient {

    private String streamsParams;

    public BinanceWebSocketClient(URI serverUri, String streamsParams) {
        super(serverUri);
        this.streamsParams = streamsParams;
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("{\n" +
                "  \"method\": \"SUBSCRIBE\",\n" +
                "  \"params\": " + streamsParams + ",\n" +
                "  \"id\": 1\n" +
                "}");
        System.out.println("opened connection");
    }

    @Override
    public void onMessage(String s) {
//        System.out.println("received: " + s);

        TradeStreamEntity tradeStreamEntity = new Gson().fromJson(s, TradeStreamEntity.class);
        if (null != tradeStreamEntity.getData())
            StatisticsCalculator.addStatistics(tradeStreamEntity);

    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        send("{\n" +
                "  \"method\": \"UNSUBSCRIBE\",\n" +
                "  \"params\": " + streamsParams + ",\n" +
                "  \"id\": 312\n" +
                "}");
        // The close codes are documented in class org.java_websocket.framing.CloseFrame

        System.out.println(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception e) {

        // The close codes are documented in class org.java_websocket.framing.CloseFrame

        System.out.println(e);
    }
}
