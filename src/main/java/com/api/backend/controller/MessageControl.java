package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.MessageModel;
import com.api.backend.services.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageControl {
    
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<MessageModel> sendMessage(@RequestBody MessageModel message) {
        MessageModel savedMessage = messageService.saveMessage(message);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    @GetMapping("/chat/{classId}")
    public List<MessageModel> getMessages(@PathVariable int classId) {
        return messageService.getMessagesByClass(classId);
    }
}

