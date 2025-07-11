package com.api.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.api.backend.services.ActiveUserService;

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
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Rutas públicas
                        .requestMatchers("/login").permitAll() // Rutas públicas
                        .requestMatchers("/**").permitAll() // Rutas públicas
                        .anyRequest().authenticated() // Las demás rutas requieren autenticación
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(successHandler) // Usar el handler personalizado para el éxito del login OAuth2
                );

        return http.build();
    }

    // Definir configuración global de CORS
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("https://frontend-hogwarts.vercel.app"); // Permitir tu frontend
        corsConfiguration.addAllowedHeader("*"); // Permitir todos los headers
        corsConfiguration.addAllowedMethod("*"); // Permitir todos los métodos (GET, POST, etc.)
        corsConfiguration.setAllowCredentials(true); // Permitir cookies y autenticación

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}