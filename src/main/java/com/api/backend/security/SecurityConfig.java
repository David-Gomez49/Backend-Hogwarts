package com.api.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthSuccess successHandler;

    public SecurityConfig(AuthSuccess successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors().and()
            .csrf().disable() // Habilitar CORS y deshabilitar CSRF
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/", "/login**", "/error**", "/css/**", "/js/**", "/images/**").permitAll() // Rutas públicas
                .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
            )
            .oauth2Login(oauth2Login -> oauth2Login
                .successHandler(successHandler) // Usar el handler personalizado para el éxito del login OAuth2
            )
            .headers(headers -> headers
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true) // Incluir subdominios
                    .maxAgeInSeconds(31536000) // Tiempo de vida de 1 año en segundos
                )
            );

        return http.build();
    }

    // Definir configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Especifica los orígenes permitidos (frontend)
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://backend-hogwarts.onrender.com"));
        // Especifica los métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        // Especifica los encabezados permitidos
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        // Activa la inclusión de credenciales
        configuration.setAllowCredentials(true); // Esto establece el encabezado Access-Control-Allow-Credentials: true

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica la configuración a todas las rutas

        return source;
    }
}
