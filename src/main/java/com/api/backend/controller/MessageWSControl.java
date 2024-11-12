

package com.api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.api.backend.model.MessageModel;
import com.api.backend.services.MessageService;

@Controller
public class MessageWSControl {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/send")
    @SendTo("/topic/group")
    public MessageModel sendMessage(MessageModel message) {
        // Guardar el mensaje en la base de datos
        MessageModel savedMessage = messageService.saveMessage(message);

        // Retornar el mensaje guardado para que sea enviado a los suscriptores
        return savedMessage;
    }
}
