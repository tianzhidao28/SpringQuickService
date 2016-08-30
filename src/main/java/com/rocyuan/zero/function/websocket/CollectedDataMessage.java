package com.rocyuan.zero.function.websocket;

import com.rocyuan.zero.domain.CollectedData;
import org.springframework.web.socket.WebSocketMessage;

/**
 * Created by rocyuan on 15/12/11.
 */
public class CollectedDataMessage implements WebSocketMessage<CollectedData> {
    @Override
    public CollectedData getPayload() {
        return null;
    }

    @Override
    public int getPayloadLength() {
        return 0;
    }

    @Override
    public boolean isLast() {
        return false;
    }
}
