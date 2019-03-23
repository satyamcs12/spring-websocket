package com.abhishekheaven.spring.websocket.server.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;


@Component
public class TomcatWebServerFactoryImpl implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Autowired
    private TomcatConnectorCustomizerImpl tomcatConnectorCustomizerImpl;

    @Override
    public void customize(TomcatServletWebServerFactory tomcatServletWebServerFactory) {

        tomcatServletWebServerFactory.addConnectorCustomizers(tomcatConnectorCustomizerImpl);

    }

}
