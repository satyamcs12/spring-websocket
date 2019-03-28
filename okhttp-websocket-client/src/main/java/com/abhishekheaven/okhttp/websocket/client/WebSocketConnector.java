package com.abhishekheaven.okhttp.websocket.client;

import com.abhishekheaven.okhttp.websocket.client.listener.WebSocketListenerExtension;
import com.abhishekheaven.okhttp.websocket.client.ssl.SSLSocketFactoryWrapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebSocketConnector {

    X509TrustManager trustManager;

    public void connect(String url) throws Exception{

        SSLContext sslContext = getSSLContext();
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(sslContext);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS).sslSocketFactory(sslSocketFactory, trustManager)
                .build();


        Request request = new Request.Builder()
                .url(url)
                .build();
        WebSocketListenerExtension webSocketListenerExtension = new WebSocketListenerExtension();
        client.newWebSocket(request, webSocketListenerExtension);

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
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
