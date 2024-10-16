package com.demo.serversentevents.handler;

import com.demo.serversentevents.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {

    Map<String, WebSocketSession> socketMap = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connected: {}", session.getId());
        String clientId = WebSocketUtil.getParam(session.getUri(), "client-id");
        if (clientId != null) {
            socketMap.put(clientId, session);
        } else {
            session.sendMessage(new TextMessage("Server Response: Client-Id not present. Closing connection!"));
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received: {}", message.getPayload());
        String targetId = WebSocketUtil.getParam(session.getUri(), "target-client-id");
        if (targetId != null && socketMap.containsKey(targetId)) {
            socketMap.get(targetId).sendMessage(message);
        } else {
            session.sendMessage(new TextMessage("Server Response: Target-ID not present or Target-ID has not connected yet. Closing connection!"));
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Disconnected: " + session.getId());
        String clientId = WebSocketUtil.getParam(session.getUri(), "client-id");
        if (clientId != null && socketMap.containsKey(clientId)) {
            socketMap.remove(clientId);
        }
    }
}
