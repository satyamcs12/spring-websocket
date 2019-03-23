package com.abhishekheaven.spring.websocket.server.configuration;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatConnectorCustomizerImpl implements TomcatConnectorCustomizer {
    @Override
    public void customize(Connector connector) {
        String portVal = System.getenv("SERVER_PORT");
        int port = Integer.parseInt(portVal);
        connector.setPort(port);
        connector.setProperty("SSLEnabled","true");
        connector.setScheme("https");
        connector.setProperty("defaultSSLHostConfigName",System.getenv("HOSTNAME_PRIMARY"));
        SSLHostConfig sslHostConfig = new SSLHostConfig();
        sslHostConfig.setHostName(System.getenv("HOSTNAME_PRIMARY"));
        sslHostConfig.setCertificateKeystoreFile(System.getenv("LOCALHOST_KEYSTORE_JKS_FILE"));
        sslHostConfig.setCertificateKeystorePassword(System.getenv("STORE_PASSWORD"));
        sslHostConfig.setCertificateVerification(System.getenv("ENABLE_CLIENT_AUTH"));
        sslHostConfig.setTruststorePassword(System.getenv("STORE_PASSWORD"));
        sslHostConfig.setTruststoreFile(System.getenv("LOCALHOST_TRUSTSTORE_JKS_FILE"));
        SSLHostConfig sslHostConfig1 = new SSLHostConfig();
        sslHostConfig1.setHostName(System.getenv("HOSTNAME_SECONDARY"));
        sslHostConfig1.setCertificateKeystoreFile(System.getenv("FQDN_KEYSTORE_JKS_FILE"));
        sslHostConfig1.setCertificateKeystorePassword(System.getenv("STORE_PASSWORD"));
        sslHostConfig1.setCertificateVerification(System.getenv("ENABLE_CLIENT_AUTH"));
        connector.addSslHostConfig(sslHostConfig1);
        connector.addSslHostConfig(sslHostConfig);
        System.out.println("port is : "+connector.getPort());
        System.out.println("scheme is : "+connector.getScheme());


    }
}
