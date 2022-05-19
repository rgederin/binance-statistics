package com.binance;


import com.binance.service.StatisticsService;
import com.binance.websocket.BinanceWebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootApplication
public class BinanceStatisticsApplication {



    public static void main(String[] args) throws URISyntaxException {
        ConfigurableApplicationContext appContext = SpringApplication.run(BinanceStatisticsApplication.class, args);


        StatisticsService service = appContext.getBean(StatisticsService.class);

        List<String> symbols = service.getBinanceSymbols()
                .getBinanceSymbols()
                .stream()
                .limit(200)
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
//        BinanceWebSocketClient c = new BinanceWebSocketClient(new URI(
//                "wss://stream.binance.com:9443/ws/ethusdt@trade"));

                BinanceWebSocketClient c = new BinanceWebSocketClient(new URI(webSocketUri.toString()), webSocketParams.toString(), service);
        c.connect();

      //  Thread.sleep(30 * 1000);

     //   c.close();
    }

}
