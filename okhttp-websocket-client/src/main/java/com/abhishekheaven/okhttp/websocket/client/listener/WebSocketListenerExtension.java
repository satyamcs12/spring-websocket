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
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WebSocketListenerExtension extends WebSocketListener {

    X509TrustManager trustManager;

    private Map<String, TopicHandler> topics = new HashMap<String, TopicHandler>();
    private CloseHandler closeHandler;
    private WebSocket webSocket;

    private MessageSendSubscribeHandler messageSendSubscribeHandler;


    public void connect(String url) throws Exception{

        SSLContext sslContext = getSSLContext();
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(sslContext);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS).sslSocketFactory(sslSocketFactory, trustManager)
                .build();


        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newWebSocket(request, this);

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response){
        System.out.println("Websocket Connection open..");
        this.webSocket = webSocket;
        messageSendSubscribeHandler = new MessageSendSubscribeHandler(webSocket);
        messageSendSubscribeHandler.sendConnectMessage(webSocket);
        messageSendSubscribeHandler.subscribe("/topic/a");
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
        t.printStackTrace();
    }

    private SSLSocketFactory getSSLSocketFactory(SSLContext sslContext) {
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        SNIHostName serverName = new SNIHostName(System.getenv("SNI_HOSTNAME"));
        List<SNIServerName> serverNames = new ArrayList<>(1);
        serverNames.add(serverName);
        SSLParameters params = new SSLParameters();
        params.setServerNames(serverNames);
        SSLSocketFactory wrappedSSLSocketFactory = new SSLSocketFactoryWrapper(sslSocketFactory, params);
        return wrappedSSLSocketFactory;
    }

    private SSLContext getSSLContext() throws Exception{
        SSLContext sslContext;
        sslContext = SSLContext.getInstance("TLS");

        //Creating keystore
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(null);
        InputStream ksIn = new FileInputStream(System.getenv("KEYSTORE_JKS_PATH"));
        keystore.load(ksIn,System.getenv("KEYSTORE_PASSWORD").toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keystore, System.getenv("KEYSTORE_PASSWORD").toCharArray());

        //Creating Truststore
        KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(null);
        String truststorePath = System.getenv("TRUSTSTORE_JKS_PATH");
        System.out.println("truststore path : " + truststorePath);
        InputStream tsIn = new FileInputStream(truststorePath);
        truststore.load(tsIn, System.getenv("TRUSTSTORE_PASSWORD").toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(truststore);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        trustManager = (X509TrustManager) trustManagers[0];

        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslContext;
    }

}
