package com.api.backend.security;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    private final String SECRET_KEY = "Xx_ElPalacioDelVandolero_xX"; // Cambia esto por una clave más segura
    private final long EXPIRATION_TIME = 86400000; // 1 día

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        System.out.println("----------------------------------------------------------------");
        System.out.println("SE LLAMO LA FUNCION");
        System.out.println("----------------------------------------------------------------");
        /*return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
                */
                System.out.println("---------------dd-d-d-d-d-d-d--d-d--d-d-");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // Se usa el email como subject
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Fecha de expiración
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Firma con clave secreta y algoritmo HS256
                .compact();

        
    }
}
