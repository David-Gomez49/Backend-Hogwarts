package com.api.backend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .cors().and() // Asegurarse de habilitar CORS explícitamente
                .csrf().disable() // Deshabilitar CSRF (opcional, depende de tu caso)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(HttpMethod.OPTIONS, "/", "/login**", "/error**", "/css/**", "/js/**", "/images/**").permitAll() // Rutas públicas
                .anyRequest().authenticated() // Las demás rutas requieren autenticación
                )
                .oauth2Login(oauth2Login
                        -> oauth2Login
                        .successHandler(successHandler) // Usar el handler personalizado para el éxito del login OAuth2
                );

        return http.build();
    }

    // Definir configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Permitir solo este origen
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Permitir todos los encabezados
        configuration.setAllowCredentials(true); // Permitir credenciales
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
