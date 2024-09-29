package com.api.backend.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.api.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthSuccess implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserService userService;

    public AuthSuccess(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        // Obtenemos los detalles del usuario autenticado (OAuth2User)
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Extraer el email, nombre y apellido del usuario
        String email = (String) oAuth2User.getAttribute("email");
        String name = (String) oAuth2User.getAttribute("given_name");   // nombre de pila
        String lastname = (String) oAuth2User.getAttribute("family_name"); // apellido
        String profilePictureUrl = (String) oAuth2User.getAttribute("picture"); // URL de la foto de perfil

        // Verificar si el usuario está registrado en la base de datos
        boolean userValid = userService.existsByEmail(email);
        String token = jwtService.generateToken(email);

        // Preparar JSON con la información del usuario
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("email", email);
        responseData.put("name", name);
        responseData.put("lastname", lastname);
        responseData.put("token", token);
        responseData.put("profilePicture", profilePictureUrl);

        // Enviar JSON como respuesta
        if (userValid) {

            responseData.put("rol", userService.GetRolByEmail(email).getName());

            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
            response.setStatus(HttpStatus.OK.value());
            response.sendRedirect("http://localhost:5173/classes?token=" + token);
        } else {

            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
            response.setStatus(HttpStatus.OK.value());

            response.sendRedirect("http://localhost:5173/register?token=" + token);
        }

    }
}
