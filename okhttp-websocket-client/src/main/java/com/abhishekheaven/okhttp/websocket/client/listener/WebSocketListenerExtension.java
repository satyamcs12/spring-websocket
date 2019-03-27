package com.abhishekheaven.okhttp.websocket.client.listener;

import com.abhishekheaven.okhttp.websocket.client.handler.CloseHandler;
import com.abhishekheaven.okhttp.websocket.client.handler.MessageSendSubscribeHandler;
import com.abhishekheaven.okhttp.websocket.client.handler.TopicHandler;
import com.abhishekheaven.okhttp.websocket.client.model.StompMessage;
import com.abhishekheaven.okhttp.websocket.client.ssl.SSLSocketFactoryWrapper;
import com.abhishekheaven.okhttp.websocket.client.utils.StompMessageSerializer;
import okhttp3.*;
import okio.ByteString;

import javax.net.ssl.*;
import java.util.HashMap;
import java.util.Map;

public class WebSocketListenerExtension extends WebSocketListener {

    X509TrustManager trustManager;

    private Map<String, TopicHandler> topics = new HashMap<String, TopicHandler>();
    private CloseHandler closeHandler;
    private WebSocket webSocket;

    private MessageSendSubscribeHandler messageSendSubscribeHandler;

    @Override
    public void onOpen(WebSocket webSocket, Response response){
        System.out.println("Websocket Connection open..");
        this.webSocket = webSocket;
        messageSendSubscribeHandler = new MessageSendSubscribeHandler(webSocket);
        messageSendSubscribeHandler.sendConnectMessage(webSocket);
        messageSendSubscribeHandler.subscribe("/topic/a");
        messageSendSubscribeHandler.subscribe("/user/queue/getini");
        messageSendSubscribeHandler.sendMessage("/app/hello","This is request for initial configuration");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("MESSAGE: " + text);
        StompMessage message = StompMessageSerializer.deserialize(text);
        String topic = message.getHeader("destination");
        System.out.println("topic is.."+topic);
        System.out.println("command is .."+message.getCommand());
        System.out.println("Content is .."+message.getContent());
        System.out.println("header list is .."+message.getHeaders().keySet());
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println("MESSAGE RECEIVED bytes: " + bytes.hex());

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        //System.out.println(System.currentTimeMillis());
        System.out.println("CLOSE: " + code + " " + reason);
        webSocket.close(1000, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        System.out.println("Failed here: " + " " + t.getMessage());
        t.printStackTrace();
    }

}
