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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

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

// Método para decodificar el token y obtener el email
    public String extractEmailFromToken(String token) {
        try {
            // Clave secreta usada para firmar el token
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

            // Parseo del token JWT para obtener las claims (información dentro del token)
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key) // Configura la clave de firma
                    .build()
                    .parseClaimsJws(token) // Parsea y valida el token
                    .getBody(); // Obtiene el cuerpo del token (claims)

            // Retorna el valor del subject, que es el email en este caso
            return claims.getSubject();

        } catch (Exception e) {
            // Captura el error si ocurre algún problema durante la validación
            System.out.println("------------------------------------------------");
            System.out.println("Error al decodificar el token: " + e.getMessage());
            System.out.println("------------------------------------------------");
            e.printStackTrace();
            return null;
        }
    }

    public boolean ValidateTokenAdmin(String token){
        String actualToken = token.substring(7);
        String email = extractEmailFromToken(actualToken);
        return userService.validateAdmin(email);
    }

    public boolean ValidateTokenAdminTeacher(String token){
        String actualToken = token.substring(7);
        String email = extractEmailFromToken(actualToken);
        return (userService.validateAdmin(email) ||  userService.validateTeacher(email));
    }

}
