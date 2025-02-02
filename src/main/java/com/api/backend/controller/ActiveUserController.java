package com.api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.api.backend.security.JwtService;
import com.api.backend.services.ActiveUserService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/activeUsers")
public class ActiveUserController {

    private static final Logger logger = Logger.getLogger(ActiveUserController.class.getName());

    @Autowired
    private ActiveUserService activeUserService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/general")
    public List<String> getActiveUsers(@CookieValue(name = "JWT") String token) {

        if (jwtService.ValidateTokenAdmin(token)) {
            List<String> users = activeUserService.getActiveUsersGeneral();
            return users;
        }
        return List.of();
    }
}