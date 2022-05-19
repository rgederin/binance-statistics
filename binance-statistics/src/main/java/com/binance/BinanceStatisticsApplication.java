package com.binance;


import com.binance.service.StatisticsService;
import com.binance.websocket.BinanceWebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


import javax.annotation.PreDestroy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootApplication
public class BinanceStatisticsApplication {

    private static StatisticsService statisticsService;

    private static BinanceWebSocketClient binanceWebSocketClient;

    @PreDestroy
    public void closeWebSocketConnections(){
        System.out.println("close");
        binanceWebSocketClient.close();
    }


    public static void main(String[] args) throws URISyntaxException {
        ConfigurableApplicationContext appContext = SpringApplication.run(BinanceStatisticsApplication.class, args);

        statisticsService = appContext.getBean(StatisticsService.class);

        List<String> symbols = statisticsService.getBinanceSymbols()
                .getBinanceSymbols()
                .stream()
                .limit(500)
                .toList();


        StringBuilder webSocketUri = new StringBuilder("wss://stream.binance.com:9443/stream?streams=");
        StringBuilder webSocketParams = new StringBuilder("[");

        symbols.forEach(symbol -> {
            webSocketUri.append(symbol.toLowerCase() + "@trade/");
            webSocketParams.append("\"" + symbol.toLowerCase() + "\"" + ",");
        });
        webSocketUri.deleteCharAt(webSocketUri.length()-1);
        webSocketParams.deleteCharAt(webSocketParams.length()-1);

        webSocketParams.append("]");
        System.out.println(webSocketUri);
        System.out.println(webSocketParams);


                binanceWebSocketClient = new BinanceWebSocketClient(new URI(webSocketUri.toString()), webSocketParams.toString(), statisticsService);
        binanceWebSocketClient.connect();
    }
}
