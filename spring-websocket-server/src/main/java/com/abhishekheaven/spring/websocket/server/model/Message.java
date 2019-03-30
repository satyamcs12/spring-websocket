package com.abhishekheaven.spring.websocket.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    @JsonProperty("name")
    private String name;

    public Message(String name) {
        this.name = name;
    }

    public Message() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
