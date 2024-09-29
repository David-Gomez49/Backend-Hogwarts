package com.api.backend.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.api.backend.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthSuccess implements AuthenticationSuccessHandler {

    // Aquí puedes inyectar el servicio de usuario para verificar si está registrado
    

    private final UserService userService;

    public AuthSuccess(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Obtener el correo o la identidad del usuario autenticado
        String email = authentication.getName();

        // Verificar si el usuario ya está registrado en la base de datos
        if (userService.existsByEmail(email)) {
            response.sendRedirect("http://localhost:5173/classes");
        } else {
            response.sendRedirect("http://localhost:5173/register");
        }
    }
}
