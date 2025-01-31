package com.api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.api.backend.services.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public String sendEmail(@RequestParam String to) {
        //emailService.sendEmail(to, "Prueba de Spring Boot", "¡Este es un correo de prueba desde Spring Boot!");
        return "Correo enviado correctamente a " + to;
    }
}
