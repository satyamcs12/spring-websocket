package com.abhishekheaven.spring.websocket.server.controller;

import com.abhishekheaven.spring.websocket.server.model.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageController(SimpMessagingTemplate template) {
        this.simpMessagingTemplate = template;
    }

    @PostMapping(value = "/hello")
    public Greeting greeting(@RequestBody String message) throws Exception {
        System.out.println("Hello");
        Thread.sleep(1000); // simulated delay
        Greeting greeting = new Greeting("Hello, " + message + "!");
        this.simpMessagingTemplate.convertAndSend("/topic/a",greeting);
        return greeting;
    }

    @MessageMapping("/hello")
    @SendToUser("/queue/getini")
    public Greeting send(String message) throws Exception {

        System.out.println(message);
        String str = message;
        Thread.sleep(1000); // simulated delay
        return new Greeting("Acknowledgement received for initial configuration..");
    }
}
