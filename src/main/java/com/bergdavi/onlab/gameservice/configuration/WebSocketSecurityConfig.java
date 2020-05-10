package com.bergdavi.onlab.gameservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * WebSocketSecurityConfig
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        
        messages
            .simpDestMatchers("/app/gameplay/*")
                .authenticated()
            .anyMessage().permitAll();

            // .simpSubscribeDestMatchers("/topic/gameplay/*")
            //     .authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
