package com.api.backend.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    private final String SECRET_KEY = "Xx_ElPalacioDelVandolero_xX"; // Cambia esto por una clave más segura
    private final long EXPIRATION_TIME = 86400000; // 1 día

    public String generateToken(String email, String name, String lastname, String picture, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", name);
        claims.put("lastname", lastname);
        claims.put("picture", picture);
        claims.put("role", role);
        System.out.println("----------------------------------------------------------------");
        System.out.println("SE LLAMO LA FUNCION");
        System.out.println("----------------------------------------------------------------");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
