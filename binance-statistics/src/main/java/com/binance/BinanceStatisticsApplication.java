package com.binance;

import com.binance.websocket.BinanceWebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class BinanceStatisticsApplication {


    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        SpringApplication.run(BinanceStatisticsApplication.class, args);

//        BinanceWebSocketClient c = new BinanceWebSocketClient(new URI(
//                "wss://stream.binance.com:9443/ws/ethusdt@trade"));

                BinanceWebSocketClient c = new BinanceWebSocketClient(new URI(
                "wss://stream.binance.com:9443/stream?streams=ethbtc@trade/bnbbtc@trade"));
        c.connect();

      //  Thread.sleep(30 * 1000);

     //   c.close();
    }

}
