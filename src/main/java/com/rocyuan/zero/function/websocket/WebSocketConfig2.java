package com.rocyuan.zero.function.websocket;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by rocyuan on 15/12/11.
 */
public class WebSocketConfig2 extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/sync","/wechat").withSockJS();
    }


    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/send","/groupsend");

    }

}
