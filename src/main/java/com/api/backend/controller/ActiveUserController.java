package com.api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.backend.security.JwtService;
import com.api.backend.services.ActiveUserService;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Set;
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
    public Set<String> getActiveUsers(@CookieValue(name = "JWT") String token) {

        if (jwtService.ValidateTokenAdmin(token)) {
            Set<String> users = activeUserService.getActiveUsersGeneral();
            return users;
        }
        return Set.of();
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> userLogout(@CookieValue(name = "JWT") String token, HttpServletResponse response) {
        String cookieValue = "JWT=; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=None";
        String email = jwtService.extractEmailFromToken(token);
        if (email != null) {
            activeUserService.removeUserGeneral(email);
        }
        response.addHeader("Set-Cookie", cookieValue);
        response.setStatus(HttpServletResponse.SC_OK);
        return ResponseEntity.ok(true);
    }
}