package com.abhishekheaven.spring.websocket.server;

import org.springframework.boot.SpringApplication;

public class App {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        System.out.println("Starting Spring Application...");
        app.run(args);
    }
}
