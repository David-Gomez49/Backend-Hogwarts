package com.api.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.backend.model.MessageModel;
import com.api.backend.services.MessageService;

@RestController
@RequestMapping("/message")
public class MessageControl {
    @Autowired
    private MessageService messageService;

    @GetMapping("/getAll")
    public List<MessageModel> obtainMessageList() {
        return messageService.obtainMessageList();
    }

    @PostMapping("/create")
    public MessageModel createMessage(@RequestBody MessageModel message) {
        return messageService.createMessage(message);
    }

    @PutMapping("/edit/{id}")
    public MessageModel updateMessage(@PathVariable int id, @RequestBody MessageModel message) {
        message.setId(id);
        return messageService.updateMessage(message);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMessage(@PathVariable int id) {
        messageService.deleteMessage(id);
    }
}
