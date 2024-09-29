package com.api.backend.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.api.backend.services.UserService;

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

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getAttribute("email");
    String name = oAuth2User.getAttribute("given_name");
    String lastname = oAuth2User.getAttribute("family_name");
    String profilePictureUrl = oAuth2User.getAttribute("picture");

    String role = userService.GetRolByEmail(email).getName();
    boolean userValid = userService.existsByEmail(email);
    String token = jwtService.generateToken(email, name, lastname, profilePictureUrl, role);

    String redirectUrl;
    if (userValid) {
        redirectUrl = "http://localhost:5173/classes?token=" + token;
    } else {
        redirectUrl = "http://localhost:5173/register?token=" + token;
    }

    // Agregar logs para depuración
    System.out.println("Redirigiendo a: " + redirectUrl);
    System.out.println("Token generado: " + token);

    System.out.println("Redirect URL: " + redirectUrl);
    response.setStatus(HttpStatus.OK.value());
    response.sendRedirect(redirectUrl);
    System.out.println("Redirect sent");
}

}
