package com.abhishekheaven.okhttp.websocket.client.handler;

import com.abhishekheaven.okhttp.websocket.client.model.Message;
import com.abhishekheaven.okhttp.websocket.client.model.StompMessage;
import com.abhishekheaven.okhttp.websocket.client.utils.StompMessageSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.WebSocket;

import java.util.HashMap;
import java.util.Map;

public class MessageSendSubscribeHandler {

    private String id = "sub-001";
    private Map<String, TopicHandler> topics = new HashMap<String, TopicHandler>();
    private WebSocket webSocket;

    public MessageSendSubscribeHandler(WebSocket webSocket){
        this.webSocket = webSocket;
    }

    public MessageSendSubscribeHandler(){
    }


    public void sendConnectMessage(WebSocket webSocket) {
        StompMessage message = new StompMessage("CONNECT");
        message.put("accept-version", "1.1");
        message.put("heart-beat", "10000,15000");
        webSocket.send(StompMessageSerializer.serialize(message));
        System.out.println("Connect message sent...");
    }

    public TopicHandler subscribe(String topic) {
        TopicHandler handler = new TopicHandler(topic);
        topics.put(topic, handler);
        if (webSocket != null) {
            sendSubscribeMessage(webSocket, topic);
        }
        return handler;
    }

    public void sendSubscribeMessage(WebSocket webSocket, String topic) {
        StompMessage message = new StompMessage("SUBSCRIBE");
        message.put("id", id);
        message.put("destination", topic);
        webSocket.send(StompMessageSerializer.serialize(message));
    }

    public void sendMessage(String topic,String content) throws JsonProcessingException {
        StompMessage message = new StompMessage("SEND");
        message.put("destination", topic);
        message.put("content-type","text/plain");
        ObjectMapper objectMapper = new ObjectMapper();
        Message message1 = new Message(content);
        String jsonInString = objectMapper.writeValueAsString(message1);
        message.setContent(jsonInString);
        webSocket.send(StompMessageSerializer.serialize(message));
    }
}
