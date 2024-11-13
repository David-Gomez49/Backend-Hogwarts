package com.api.backend.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.api.backend.model.UserModel;
import com.api.backend.services.UserService;
import com.api.backend.security.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

        // Obtener el usuario autenticado de OAuth2
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extraer la información relevante del usuario
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("given_name");
        String lastname = oAuth2User.getAttribute("family_name");
        String profilePictureUrl = oAuth2User.getAttribute("picture");

        // Crear o buscar un usuario en la base de datos
        UserModel user = new UserModel(0, name, lastname, null, null, null, null, email, null, null, null, profilePictureUrl);

        // Verificar si el usuario existe en la base de datos
        boolean userValid = userService.existsByEmail(email);

        if (!userValid) {
            // Si el usuario no existe, crearlo en la base de datos
            userService.createUser(user);
        } 

        // Generar el JWT para el usuario
        String token = jwtService.generateToken(email);
        
        // Si el token no es válido, responder con un error
        if (token == null || token.isEmpty()) {
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error al generar el token JWT");
            return;
        }

        //COOKIE----
        // Crear una cookie con el token JWT
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true);  // La cookie solo debe ser accesible a través de HTTP (no JavaScript)
        jwtCookie.setSecure(false);   // No es necesario en HTTP (solo en HTTPS)
        jwtCookie.setPath("/");       // Establecer la cookie para que esté disponible en toda la aplicación
        jwtCookie.setMaxAge(24 * 60 * 60);  // La cookie expirará después de 1 día (24 horas)

        // Añadir la cookie a la respuesta
        response.addCookie(jwtCookie);
        // Configurar el encabezado "Set-Cookie" manualmente si es necesario
        response.setHeader("Set-Cookie", "token=" + token + "; HttpOnly; Path=/; Max-Age=" + (24 * 60 * 60) + "; SameSite=None");
        //----------


        // Determinar la URL de redirección dependiendo de si el usuario tiene su perfil completo
        String redirectUrl;
        if (userService.InfoCompleteByEmail(email)) {
            // Redirigir al usuario a la página de clases si el perfil está completo
            redirectUrl = "http://localhost:5173/classes";
        } else {
            // Si no está completo, redirigir a la página de registro
            redirectUrl = "http://localhost:5173/register";
        }

        // Configurar la respuesta y agregar la cookie
        response.setStatus(HttpStatus.OK.value());  // Estado HTTP 200 (OK)

        // Redirigir al usuario a la URL correspondiente
        response.sendRedirect(redirectUrl);
    }

}
