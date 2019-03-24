package com.abhishekheaven.spring.websocket.client;

import com.abhishekheaven.spring.websocket.client.handler.MyStompSessionHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.util.ArrayList;
import java.util.List;

public class MyWebSocketClient extends Thread {

    @Override
    public void run(){
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        WebSocketStompClient stompClient = configure();
        String URL = System.getenv("url");
        if(URL == null || URL.isEmpty()){
            System.out.println("Envt. variable for URL is not set");
            URL = Constants.URL;
        }
        System.out.println("URL is : "+URL);
        System.setProperty("javax.net.ssl.keyStore",System.getenv("KEYSTORE_JKS_PATH"));
        System.setProperty("javax.net.ssl.keyStorePassword",System.getenv("KEYSTORE_PASSWORD"));
        System.setProperty("javax.net.ssl.trustStore",System.getenv("TRUSTSTORE_JKS_PATH"));
        System.setProperty("javax.net.ssl.trustStorePassword",System.getenv("TRUSTSTORE_PASSWORD"));

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        ListenableFuture<StompSession> stompSessionListenableFuture=  stompClient.connect(URL, sessionHandler);
        stompSessionListenableFuture.addCallback(new ListenableFutureCallback<StompSession>() {
            @Override
            public void onFailure(Throwable throwable) {
                //System.out.println(throwable.getStackTrace());
                throwable.printStackTrace();
                System.out.println("Could not able to connect to server...");
            }

            @Override
            public void onSuccess(StompSession stompSession) {
                System.out.println("Connection to server successful");
            }
        });
    }

    private WebSocketStompClient configure() {

        WebSocketStompClient stompClient = getStompClient();
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        long heartbeat[] = {0,0};
        stompClient.setDefaultHeartbeat(heartbeat);
        stompClient.setTaskScheduler(taskScheduler);
        return stompClient;
    }

    private WebSocketStompClient getStompClient(){

        List<Transport> transports = new ArrayList<>(1);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        WebSocketClient wsClient = new StandardWebSocketClient(container);
        transports.add(new WebSocketTransport(wsClient));
        org.springframework.web.socket.client.WebSocketClient transport = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(transport);

        return stompClient;
    }
}
