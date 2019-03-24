package com.abhishekheaven.spring.websocket.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Greeting {

    @JsonProperty("content")
    private String content;

    public Greeting(String content) {
        this.content = content;
    }

    public Greeting() {
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
