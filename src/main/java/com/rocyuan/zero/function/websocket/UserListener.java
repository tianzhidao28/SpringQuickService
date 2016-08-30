package com.rocyuan.zero.function.websocket;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by rocyuan on 15/12/11.
 */
public interface UserListener {
    void addUser(WebSocketSession session);
    void delUser(WebSocketSession session);
}
