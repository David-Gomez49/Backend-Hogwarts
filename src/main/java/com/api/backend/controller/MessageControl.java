package com.api.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import com.api.backend.model.MessageModel;

@RestController
public class MessageControl {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageModel sendMessage(@Payload MessageModel message) {
        message.setSend_date(Instant.now());
        return message;
    }

}

