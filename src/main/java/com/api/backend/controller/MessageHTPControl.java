package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.MessageModel;
import com.api.backend.services.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageHTPControl {
    
    @Autowired
    private MessageService messageService;

    @GetMapping("/getMessagesClassId/{classId}")
    public List<MessageModel> getMessages(@PathVariable int classId) {
        return messageService.getMessagesByClass(classId);
    }
}