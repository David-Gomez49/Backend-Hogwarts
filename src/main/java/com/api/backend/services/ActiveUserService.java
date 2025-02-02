package com.api.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ActiveUserService {

    private static final Logger logger = Logger.getLogger(ActiveUserService.class.getName());

    private final Set<String> activeUsersGeneral = new HashSet<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void addUserGeneral(String email) {
        logger.info("Adding user to general: " + email);
        boolean added = activeUsersGeneral.add(email);
        if (added) {
            messagingTemplate.convertAndSend("/topic/activeUsers/general", activeUsersGeneral);
        }
        logger.info("User " + email + " added to general: " + added);
    }

    public void removeUserGeneral(String email) {
    logger.info("Attempting to remove user: " + email + " from activeUsersGeneral.");
    logger.info("Current users before removal: " + activeUsersGeneral);

    boolean removed = activeUsersGeneral.remove(email);

    if (removed) {
        logger.info("User " + email + " successfully removed.");
        messagingTemplate.convertAndSend("/topic/activeUsers/general", activeUsersGeneral);
    } else {
        logger.warning("User " + email + " was NOT found in activeUsersGeneral.");
    }

    logger.info("Current users after removal: " + activeUsersGeneral);
}

    public Set<String> getActiveUsersGeneral() {
        logger.info("Getting active users general: " + activeUsersGeneral.size());
        return activeUsersGeneral;
    }
}
