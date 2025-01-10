package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    private ChatWebSocketHandler chatWebSocketHandler = new ChatWebSocketHandler();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // registry.addHandler(new ChatWebSocketHandler(), "/ws").setAllowedOrigins("*");
        registry.addHandler(chatWebSocketHandler, "/ws")
                .setAllowedOrigins("*"); // 允许所有来源连接

    }

}
