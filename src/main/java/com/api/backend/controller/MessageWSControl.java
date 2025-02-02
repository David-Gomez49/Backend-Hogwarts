
package com.api.backend.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.backend.model.MessageModel;
import com.api.backend.security.JwtService;
import com.api.backend.services.ActiveUserService;
import com.api.backend.services.MessageService;
import com.api.backend.services.UserService;

@Controller
public class MessageWSControl {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    

    @Autowired
    private ActiveUserService activeUserService;

    @MessageMapping("/send/{classId}")
    @SendTo("/topic/class/{classId}")
    public MessageModel sendMessage(@RequestParam String classId, MessageModel message) {
        // Guardar el mensaje en la base de datos
        MessageModel savedMessage = messageService.saveMessage(message);

        // Retornar el mensaje guardado para que sea enviado a los suscriptores
        return savedMessage;
    }

    @MessageMapping("/changeRole")
    @SendTo("/topic/roleChange")
    public String notifyRoleChange(String message) {
        return message;
    }

    @MessageMapping("/sendCustomMessage")
    @SendTo("/topic/message/{email}")
    public void sendCustomMessage(String email, String title, String message) {
        userService.sendCustomMessage(email, title, message);
    }

    @MessageMapping("/activeUsers/general")
    @SendTo("/topic/activeUsers/general")
    public Set<String> getActiveUsers() {
        return activeUserService.getActiveUsersGeneral();
    }

}
