package com.api.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.services.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;

@Service
public class JwtService {

    @Autowired
    private UserService userService;

    private final String SECRET_KEY = "Xx_ElPalacioDelVandolero_xX_xx_MuchoMasSegura_xx";
    private final long EXPIRATION_TIME = 86400000; // 1 día

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        try {
            // Verifica que la clave tenga el tamaño adecuado
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha actual
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiración en 1 día
                    .signWith(key, SignatureAlgorithm.HS256) // Firma con la clave y algoritmo HS256
                    .compact();
        } catch (InvalidKeyException e) {
            return null;
        }
    }

// Método para decodificar el token y obtener el email
    public String extractEmailFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key) // Configura la clave de firma
                    .build()
                    .parseClaimsJws(token) // Parsea y valida el token
                    .getBody(); // Obtiene el cuerpo del token (claims)

            return claims.getSubject();

        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException | WeakKeyException | IllegalArgumentException e) {
            
            return null;
        }
    }

    public boolean ValidateTokenAdmin(String token){
        String email = extractEmailFromToken(token);
        return userService.validateAdmin(email);
    }

    public boolean ValidateTokenAdminTeacher(String token){
        String email = extractEmailFromToken(token);
        return (userService.validateAdmin(email) ||  userService.validateTeacher(email));
    }


}
