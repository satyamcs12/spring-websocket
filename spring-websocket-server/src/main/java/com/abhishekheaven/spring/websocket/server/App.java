package com.abhishekheaven.spring.websocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class App {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        System.out.println("Starting Spring Application...");
        app.run(args);
    }
}
