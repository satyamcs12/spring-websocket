package com.abhishekheaven.okhttp.websocket.client.listener;

import com.abhishekheaven.okhttp.websocket.client.model.Greeting;

public interface StompMessageListener {
    void onMessage(Greeting greeting);

}
