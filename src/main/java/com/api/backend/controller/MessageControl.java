package com.api.backend.controller;

import com.api.backend.model.MessageModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class MessageControl {

    @MessageMapping("/send") // El frontend enviará mensajes aquí
    @SendTo("/topic/group") // Los mensajes serán enviados a todos los suscriptores de "/topic/group"
    public MessageModel sendMessage(MessageModel message) {
        message.setSend_date(Instant.now()); // Establecer la fecha de envío
        return message; // Enviar mensaje a todos los suscriptores
    }
}
