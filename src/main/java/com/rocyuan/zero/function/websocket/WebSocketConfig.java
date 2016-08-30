package com.rocyuan.zero.function.websocket;

import com.rocyuan.zero.domain.CollectedData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * Created by rocyuan on 15/12/11.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(collectedDataHandler(),"/websocket/wechat").addInterceptors(loginedUserInterceptor());
        webSocketHandlerRegistry.addHandler(collectedDataHandler(),"/sockjs/wechat").addInterceptors(loginedUserInterceptor()).withSockJS();
    }

    @Bean
    public LoginedUserInterceptor loginedUserInterceptor(){
        return new LoginedUserInterceptor();
    }

    @Bean
    public CollectedDataHandler collectedDataHandler(){
        return new CollectedDataHandler();
    }

}
