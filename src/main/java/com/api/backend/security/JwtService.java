package com.api.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY = "Xx_ElPalacioDelVandolero_xX_xx_MuchoMasSegura_xx"; 
    private final long EXPIRATION_TIME = 86400000; // 1 día

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        
        try {
            // Verifica que la clave tenga el tamaño adecuado
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

            // Depuración de la generación del token
            System.out.println("------------------------------------------------");
            System.out.println("Generando token con email: " + email);
            System.out.println("------------------------------------------------");

            // Construcción del token JWT
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha actual
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiración en 1 día
                    .signWith(key, SignatureAlgorithm.HS256) // Firma con la clave y algoritmo HS256
                    .compact();
        } catch (Exception e) {
            // Captura y muestra el error si algo falla
            System.out.println("------------------------------------------------");
            System.out.println("Error al generar el token: " + e.getMessage());
            System.out.println("------------------------------------------------");
            e.printStackTrace();
            return null;
        }
    }
}
