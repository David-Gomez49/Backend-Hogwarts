package com.api.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;


@Service
public class ActiveUserService {

    private final List<String> activeUsersGeneral = new ArrayList<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void addUserGeneral(String email) {
        if (!activeUsersGeneral.contains(email)) {
            activeUsersGeneral.add(email);
            messagingTemplate.convertAndSend("/topic/activeUsers/general", activeUsersGeneral);
        }
    }

    public void removeUserGeneral(String email) {
        if (activeUsersGeneral.contains(email)) {
            activeUsersGeneral.remove(email);
            messagingTemplate.convertAndSend("/topic/activeUsers/general", activeUsersGeneral);
        }
    }

    public List<String> getActiveUsersGeneral() {
        return activeUsersGeneral;
    }
}
