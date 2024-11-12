package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.MessageModel;
import com.api.backend.repository.MessageRepo;

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    public List<MessageModel> obtainMessageList() {
        return (List<MessageModel>) messageRepo.findAll();
    }

    public MessageModel saveMessage(MessageModel message) {
        return messageRepo.save(message);
    }

    public List<MessageModel> getMessagesByClass(int classId) {
        return messageRepo.findByClasses_Id(classId);
    }

    public MessageModel createMessage(MessageModel message) {
        return messageRepo.save(message);
    }

    public MessageModel updateMessage(MessageModel message) {
        return messageRepo.save(message);
    }

    public void deleteMessage(int id) {
        messageRepo.deleteById(id);
    }
}
