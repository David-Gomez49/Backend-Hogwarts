package com.api.backend.security;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    private final AuthSuccess successHandler;

    public SecurityConfig(AuthSuccess successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/", "/login**", "/error**").permitAll() // Permitir acceso a login, raíz y errores
                .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
            )
            .oauth2Login(oauth2Login -> 
                oauth2Login
                    .successHandler(successHandler) // Usar el handler personalizado
            );

        return http.build();
    }
}

