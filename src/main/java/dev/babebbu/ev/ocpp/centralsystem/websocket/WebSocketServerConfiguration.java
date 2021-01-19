package dev.babebbu.ev.ocpp.centralsystem.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketServerConfiguration implements WebSocketConfigurer {

    private final OcppWebSocketHandler ocppWebSocketHandler;

    public WebSocketServerConfiguration(OcppWebSocketHandler ocppWebSocketHandler) {
        this.ocppWebSocketHandler = ocppWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(ocppWebSocketHandler, "/ocpp").setAllowedOrigins("*");
    }
}
