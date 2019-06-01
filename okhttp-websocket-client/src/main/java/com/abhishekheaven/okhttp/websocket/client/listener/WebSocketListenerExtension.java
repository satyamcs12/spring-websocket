package com.abhishekheaven.okhttp.websocket.client.listener;

import com.abhishekheaven.okhttp.websocket.client.Constants;
import com.abhishekheaven.okhttp.websocket.client.WebSocketConnector;
import com.abhishekheaven.okhttp.websocket.client.handler.CloseHandler;
import com.abhishekheaven.okhttp.websocket.client.handler.ConfigurationMessageHandler;
import com.abhishekheaven.okhttp.websocket.client.handler.MessageSendSubscribeHandler;
import com.abhishekheaven.okhttp.websocket.client.model.StompMessage;
import com.abhishekheaven.okhttp.websocket.client.utils.StompMessageSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.*;
import okio.ByteString;

import javax.net.ssl.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class WebSocketListenerExtension extends WebSocketListener {

    X509TrustManager trustManager;

    private CloseHandler closeHandler;
    private WebSocket webSocket;

    private MessageSendSubscribeHandler messageSendSubscribeHandler;

    private boolean isThreadAlive = false;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        System.out.println("Websocket Connection open..");
        this.webSocket = webSocket;
        isThreadAlive = true;
        messageSendSubscribeHandler = new MessageSendSubscribeHandler(webSocket);
        messageSendSubscribeHandler.sendConnectMessage(webSocket);
        messageSendSubscribeHandler.subscribe("/topic/a");
        messageSendSubscribeHandler.subscribe("/user/queue/getini");
        try {
            messageSendSubscribeHandler.sendMessage("/app/hello", "This is request for initial configuration");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        if (text.equalsIgnoreCase("\n")) {
            System.out.println("Heartbeat from server");
            System.out.println("Current time: " + LocalDateTime.now());
        } else {
            System.out.println("MESSAGE: " + text);
            //System.out.println("text length: " + text.length());
            StompMessage message = StompMessageSerializer.deserialize(text);
            String command = message.getCommand();
            if (command != null && command.equalsIgnoreCase("MESSAGE")) {
                String destination = message.getHeader("destination");
                ConfigurationMessageHandler configurationMessageHandler = new ConfigurationMessageHandler();
                if (destination.equalsIgnoreCase("/user/queue/getini")) {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            configurationMessageHandler.handleInitialConfig(message.getContent());
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                    //configurationMessageHandler.handleInitialConfig(message.getContent());
                } else {
                    configurationMessageHandler.handleNotificationConfig(message.getContent());
                }
            } else if (command != null && command.equalsIgnoreCase("CONNECTED")) {
                System.out.println("Command is CONNECTED");
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        while (isThreadAlive) {
                            try {
                                messageSendSubscribeHandler.sendMessage("/app/heartbeat", "Heartbeat from client");
                                System.out.println("Sleeping for 10 sec");
                                Thread.sleep(10000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }


        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println("MESSAGE RECEIVED bytes: " + bytes.hex());

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        //System.out.println(System.currentTimeMillis());
        System.out.println("CLOSE: " + code + " " + reason);
        System.out.println("onClosing time: " + LocalDateTime.now());
        isThreadAlive = false;
        webSocket.close(1000, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {

        webSocket.close(1000, null);
        isThreadAlive = false;
        System.out.println("onFailure time: " + LocalDateTime.now());
        System.out.println("Failed here: " + " " + t.getMessage());
        boolean isConnected = false;
        try {
            System.out.println("Waiting for 60 sec to reconnect");
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        reconnect();
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        System.out.println("onClosed time: " + LocalDateTime.now());
        isThreadAlive = false;
        System.out.println("Websocket session closed with code : " + code + " reason " + reason);
        try {
            System.out.println("Waiting for 60 sec to reconnect");
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        reconnect();
    }


    public boolean reconnect() {
        WebSocketConnector connector = new WebSocketConnector();

        String URL = System.getenv("url");
        if (URL == null) {
            System.out.println("Envt. variable for URL is not set");
            URL = Constants.URL;
        }
        //URL = "wss://localhost:8081/hello/websocket";
        System.out.println("URL is : " + URL);
        try {
            connector.connect(URL);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
