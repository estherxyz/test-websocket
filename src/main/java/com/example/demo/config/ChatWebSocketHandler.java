package com.example.demo.config;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ChatWebSocketHandler extends TextWebSocketHandler {
    
    // private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        session.getAttributes().put("userId", userId); // 将用户ID存入会话属性
        sessions.put(userId, session); // 将会话存入映射
        // session.sendMessage(new TextMessage("Connected as user: " + userId));
        System.out.println("Connected as user: " + userId);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received: " + message.getPayload());

        // 假设消息是 JSON 格式，解析出目标用户ID和消息内容
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> payload = objectMapper.readValue(message.getPayload(), Map.class);

        String targetUserId = payload.get("targetUserId"); // 从消息中提取目标用户ID
        String content = payload.get("message"); // 从消息中提取实际消息内容

        if (targetUserId != null && sessions.containsKey(targetUserId)) {
            // 找到目标用户的 WebSocketSession 并发送消息
            WebSocketSession targetSession = sessions.get(targetUserId);
            if (targetSession != null && targetSession.isOpen()) {
                // targetSession.sendMessage(new TextMessage("Message to you: " + content));
                System.out.println("Message to you: " + content);
            } else {
                System.out.println("Target session for user " + targetUserId + " is not available.");
            }
        } else {
            System.out.println("No session found for user: " + targetUserId);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session); // 从请求中解析用户ID
        // sessions.remove(userId); // 从Map中移除
        System.out.println("User disconnected: " + userId);
    }


    private String getUserIdFromSession(WebSocketSession session) {
        // 从请求参数中提取用户ID
        
        String query = session.getUri().getQuery(); // e.g., "userId=u123"
        if (query != null && query.contains("userId=")) {
            return query.split("userId=")[1];
        }
        return "anonymous"; // 如果没有指定用户ID，设为匿名
    }

    
    public void sendMessageToUser(String userId, String message) {
        WebSocketSession session = sessions.get(userId);
        System.out.println("check session " + session.toString());
        System.out.println("check session open " + session.isOpen());
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User " + userId + " is not connected.");
        }
    }
}
