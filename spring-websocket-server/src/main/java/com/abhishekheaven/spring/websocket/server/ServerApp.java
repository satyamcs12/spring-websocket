package com.abhishekheaven.spring.websocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ServerApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ServerApp.class);
        System.out.println("Starting Spring Application...");
        System.out.println("Commit1");
        app.run(args);
    }
}
