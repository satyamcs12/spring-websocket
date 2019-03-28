package com.abhishekheaven.okhttp.websocket.client.handler;

import com.abhishekheaven.okhttp.websocket.client.listener.StompMessageListener;
import com.abhishekheaven.okhttp.websocket.client.model.Greeting;

import java.util.HashSet;
import java.util.Set;

public class TopicHandler {
    private String topic;
    private Set<StompMessageListener> listeners = new HashSet<StompMessageListener>();

    public TopicHandler(String topic) {
        this.topic = topic;
    }

    public TopicHandler() {

    }

    public String getTopic() {
        return topic;
    }

    public void addListener(StompMessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(StompMessageListener listener) {
        listeners.remove(listener);
    }

    public void onMessage(Greeting greeting) {
        for (StompMessageListener listener : listeners) {
            listener.onMessage(greeting);
        }
    }
}
