package com.abhishekheaven.spring.websocket.server.component;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatConnectorCustomizerImpl implements TomcatConnectorCustomizer {
    @Override
    public void customize(Connector connector) {
        String portVal = "8081";
        int port = Integer.parseInt(portVal);
        connector.setPort(port);
        //connector.setProperty("SSLEnabled","true");
        connector.setScheme("http");
//        connector.setProperty("defaultSSLHostConfigName",System.getenv("HOSTNAME_PRIMARY"));
//
//        //Setting SSL Configurations for Primary Hostname
//        SSLHostConfig sslHostConfig = new SSLHostConfig();
//        System.out.println("Primary Host name: "+System.getenv("HOSTNAME_PRIMARY"));
//        sslHostConfig.setHostName(System.getenv("HOSTNAME_PRIMARY"));
//        sslHostConfig.setCertificateKeystoreFile(System.getenv("HOSTNAME_PRIMARY_KEYSTORE_JKS_FILE"));
//        sslHostConfig.setCertificateKeystorePassword(System.getenv("HOSTNAME_PRIMARY_KEYSTORE_PASSWORD"));
//        sslHostConfig.setCertificateVerification(System.getenv("HOSTNAME_PRIMARY_ENABLE_CLIENT_AUTH"));
//        sslHostConfig.setTruststoreFile(System.getenv("HOSTNAME_PRIMARY_TRUSTSTORE_JKS_FILE"));
//        sslHostConfig.setTruststorePassword(System.getenv("HOSTNAME_PRIMARY_TRUSTSTORE_PASSWORD"));
//        connector.addSslHostConfig(sslHostConfig);
//
//        //Setting SSL Configurations for Secondary Hostname
//        SSLHostConfig sslHostConfig1 = new SSLHostConfig();
//        System.out.println("Secondary Host name: "+System.getenv("HOSTNAME_SECONDARY"));
//        sslHostConfig1.setHostName(System.getenv("HOSTNAME_SECONDARY"));
//        sslHostConfig1.setCertificateKeystoreFile(System.getenv("HOSTNAME_SECONDARY_KEYSTORE_JKS_FILE"));
//        sslHostConfig1.setCertificateKeystorePassword(System.getenv("HOSTNAME_SECONDARY_KEYSTORE_PASSWORD"));
//        sslHostConfig1.setCertificateVerification(System.getenv("HOSTNAME_SECONDARY_ENABLE_CLIENT_AUTH"));
//        sslHostConfig1.setTruststoreFile(System.getenv("HOSTNAME_SECONDARY_TRUSTSTORE_JKS_FILE"));
//        sslHostConfig1.setTruststorePassword(System.getenv("HOSTNAME_SECONDARY_TRUSTSTORE_PASSWORD"));
//        connector.addSslHostConfig(sslHostConfig1);

        System.out.println("port is : "+connector.getPort());
        System.out.println("scheme is : "+connector.getScheme());
    }
}
