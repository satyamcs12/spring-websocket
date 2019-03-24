package com.abhishekheaven.spring.websocket.client.handler;

import com.abhishekheaven.spring.websocket.client.Constants;
import com.abhishekheaven.spring.websocket.client.model.Greeting;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
public class MyStompSessionHandler extends StompSessionHandlerAdapter {



    StompSession session;


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.session = session;
        System.out.println("New session established : " + session.getSessionId());
        session.subscribe("/topic/a", this);
        session.subscribe("/topic/b", this);
        System.out.println("Subscribed to /topic/a");
        //session.subscribe("/queue/getini",this);
        session.subscribe("/user/queue/getini", this);
        session.send("/app/hello","Hello Server..From Client" );
        System.out.println("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println("Got an exception" + exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        if (throwable instanceof ConnectionLostException) {
            System.out.println("Connection is lost for session id: "+stompSession.getSessionId());
        }
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("Handle Frame:");
        Greeting msg = (Greeting) payload;
        System.out.println("Received : " + msg.getContent());
        //session.send("/app/hello", getSampleMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        //System.out.println("header is  " + headers.getDestination());
        return Greeting.class;
    }
}
