package com.binance;


import com.binance.service.StatisticsService;
import com.binance.websocket.BinanceWebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


import javax.annotation.PreDestroy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class BinanceStatisticsApplication {
    private static List<BinanceWebSocketClient> binanceWebSocketClients;

    @PreDestroy
    public void closeWebSocketConnections() {
        binanceWebSocketClients.forEach(WebSocketClient::close);
    }

    public static void main(String[] args) throws URISyntaxException {
        ConfigurableApplicationContext appContext = SpringApplication.run(BinanceStatisticsApplication.class, args);
        binanceWebSocketClients = new ArrayList<>();

        /**
         * Get all symbols in order to build combined streams
         */
        StatisticsService statisticsService = appContext.getBean(StatisticsService.class);

        List<String> symbols = statisticsService.getBinanceSymbols()
                .getBinanceSymbols()
                .stream()
                .toList();

        log.info("receiving amount of symbols: {}", symbols.size());

        StringBuilder webSocketUri = new StringBuilder("wss://stream.binance.com:9443/stream?streams=");
        StringBuilder webSocketParams = new StringBuilder("[");

        /**
         * Build and star web socket connections with combined streams.
         * Documentation mentioned up 1024 streams but for some reasons such amount is failing
         */
        for (int i = 0; i < symbols.size(); i++) {
            webSocketUri.append(symbols.get(i).toLowerCase() + "@trade/");
            webSocketParams.append("\"" + symbols.get(i).toLowerCase() + "\"" + ",");
            if (i % 500 == 0) {
                webSocketUri.deleteCharAt(webSocketUri.length() - 1);
                webSocketParams.deleteCharAt(webSocketParams.length() - 1);

                webSocketParams.append("]");
                binanceWebSocketClients.add(new BinanceWebSocketClient(new URI(webSocketUri.toString()), webSocketParams.toString(), statisticsService));

                webSocketUri = new StringBuilder("wss://stream.binance.com:9443/stream?streams=");
                webSocketParams = new StringBuilder("[");
            }
        }

        binanceWebSocketClients.forEach(WebSocketClient::connect);
    }
}
