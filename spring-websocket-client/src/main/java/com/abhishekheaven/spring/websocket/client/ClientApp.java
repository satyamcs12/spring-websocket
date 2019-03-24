package com.abhishekheaven.spring.websocket.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ClientApp {
    public static void main(String[] args) {
        try {
            Thread thread = new Thread(new MyWebSocketClient());
            thread.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        SpringApplication app = new SpringApplication(ClientApp.class);
        System.out.println("Starting Spring Application...");
        app.run(args);
    }
}
