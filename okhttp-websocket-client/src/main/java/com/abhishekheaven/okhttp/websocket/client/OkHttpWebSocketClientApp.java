package com.abhishekheaven.okhttp.websocket.client;

import com.abhishekheaven.okhttp.websocket.client.listener.WebSocketListenerExtension;
import okhttp3.WebSocketListener;

public class OkHttpWebSocketClientApp {
    public static void main(String[] args) {

        WebSocketListenerExtension client = new WebSocketListenerExtension();

        String URL = System.getenv("url");
        if (URL == null) {
            System.out.println("Envt. variable for URL is not set");
            URL =Constants.URL;
        }


        //URL = "wss://localhost:8081/hello/websocket";
        System.out.println("URL is : " + URL);
        try {
            client.connect(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
