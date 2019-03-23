package com.abhishekheaven.spring.websocket.server.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class EventHandler {

    @EventListener
    public void onBrokerAvailabilityEvent(BrokerAvailabilityEvent brokerAvailabilityEvent){
        System.out.println("Broker is available");
    }

    @EventListener
    public void onSessionConnectEvent(SessionConnectEvent sessionConnectEvent){
        System.out.println("Received Session connection request...");
        Message<byte[]> message = sessionConnectEvent.getMessage();
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
        System.out.println("session id: "+headers.getSessionId());
    }

    @EventListener
    public void onSessionConnectedEvent(SessionConnectedEvent sessionConnectedEvent){

        System.out.println("Session Connected: ");
        //System.out.println(sessionConnectedEvent.getMessage());
        Message<byte[]> message = sessionConnectedEvent.getMessage();
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
        System.out.println("session id: "+headers.getSessionId());
    }

    @EventListener
    public void onSessionDisConnectedEvent(SessionDisconnectEvent sessionDisconnectEvent){
        System.out.println("Session Disconnected");
        Message<byte[]> message = sessionDisconnectEvent.getMessage();
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
        System.out.println("session id: "+headers.getSessionId());
    }

}
