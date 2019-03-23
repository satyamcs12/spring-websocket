package com.abhishekheaven.spring.websocket.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        String message = "Welcome to Spring App";
        return message;
    }
}
