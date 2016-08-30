package com.rocyuan.zero.function.websocket;

import com.rocyuan.zero.domain.CollectedData;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by rocyuan on 15/12/11.
 * 收集数据之用
 */
public class CollectedDataHandler extends UserUtils implements WebSocketHandler {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    public CollectedDataHandler()
    {
        super();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        LOG.info("连接建立,{}",webSocketSession.getHandshakeHeaders().toString());
        addUser(webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        String message = webSocketMessage.getPayload().toString();
        LOG.info("receive msg = {},head={}",message,webSocketSession.getHandshakeHeaders());
        LOG.info("receive msg session ={} " + webSocketSession.getAttributes());
        //webSocketSession.sendMessage(new TextMessage("echo message"));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOG.info("handleTransportError session {},{}",webSocketSession.getHandshakeHeaders(), ExceptionUtils.getStackTrace(throwable));
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
            delUser(webSocketSession);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        delUser(webSocketSession);
        LOG.info("close session {},{}",webSocketSession.getHandshakeHeaders(),closeStatus.getReason());

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
