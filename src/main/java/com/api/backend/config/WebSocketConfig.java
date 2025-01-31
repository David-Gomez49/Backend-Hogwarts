package com.api.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app"); // Prefijo para los mensajes enviados desde el frontend
        config.enableSimpleBroker("/topic"); // Broker para el chat grupal
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Endpoint para conectar al WebSocket
                .setAllowedOrigins("https://frontend-hogwarts.vercel.app") // Permitir solicitudes desde el frontend // Permitir solicitudes desde el frontend                            
                .withSockJS(); // Uso de SockJS para compatibilidad
    }
}
