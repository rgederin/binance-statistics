package com.binance;

import com.binance.model.generated.Symbol;
import com.binance.repository.StatisticsRepository;
import com.binance.service.StatisticsService;
import com.binance.websocket.BinanceWebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@SpringBootApplication
public class BinanceStatisticsApplication {



    public static void main(String[] args) throws URISyntaxException {
        ConfigurableApplicationContext appContext = SpringApplication.run(BinanceStatisticsApplication.class, args);


        StatisticsRepository repository = appContext.getBean(StatisticsRepository.class);

        List<String> symbols = repository.getBinanceSymbols()
                .getSymbols()
                .stream()
                .limit(200)
                .map(Symbol::getSymbol)
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

                BinanceWebSocketClient c = new BinanceWebSocketClient(new URI(webSocketUri.toString()), webSocketParams.toString());
        c.connect();

      //  Thread.sleep(30 * 1000);

     //   c.close();
    }

}
