package com.binance.websocket;

import com.binance.model.gson.TradeStreamEntity;
import com.binance.service.StatisticsService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Objects;

@Slf4j
public class BinanceWebSocketClient extends WebSocketClient {

    private final StatisticsService statisticsService;
    private final String subscribeMessage;
    private final String unsubscribeMessage;

    public BinanceWebSocketClient(URI serverUri, String streamsParams, StatisticsService statisticsService) {
        super(serverUri);
        this.statisticsService = statisticsService;

        this.subscribeMessage = "{\n" +
                "  \"method\": \"SUBSCRIBE\",\n" +
                "  \"params\": " + streamsParams + ",\n" +
                "  \"id\": 1\n" + "}";

        this.unsubscribeMessage = "{\n" +
                "  \"method\": \"UNSUBSCRIBE\",\n" +
                "  \"params\": " + streamsParams + ",\n" +
                "  \"id\": 312\n" + "}";
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        send(this.subscribeMessage);
        log.info("connected to the binance web socket endpoint");
    }

    @Override
    public void onMessage(String s) {
        TradeStreamEntity tradeStreamEntity = new Gson().fromJson(s, TradeStreamEntity.class);

        if (Objects.nonNull(tradeStreamEntity.getData())) {
            statisticsService.updateSymbolsStatistics(tradeStreamEntity);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        send(this.unsubscribeMessage);
    }

    @Override
    public void onError(Exception e) {
        send(this.unsubscribeMessage);
        log.info("Error from web socket client: " + e.getMessage());
    }
}
