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

    private final ActiveUserService activeUserService;
    private final AuthSuccess successHandler;

    public SecurityConfig(AuthSuccess successHandler, ActiveUserService activeUserService) {
        this.activeUserService = activeUserService;
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
            .oauth2Login(oauth2Login
                -> oauth2Login
                .successHandler(successHandler) // Usar el handler personalizado para el éxito del login OAuth2
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Ruta para cerrar sesión
                .deleteCookies("JWT") // Eliminar la cookie del token JWT
                .logoutSuccessHandler((request, response, authentication) -> {
                    String cookieValue = String.format("JWT=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None", null, 0);
                    response.addHeader("Set-Cookie", cookieValue);
                    response.setStatus(200);
                    if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                    org.springframework.security.oauth2.core.user.OAuth2User oauth2User = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
                    String email = oauth2User.getAttribute("email").toString();
                    String email2 = oauth2User.getAttribute("email");
                    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
                    logger.info("==========================");
                    logger.info("=========" + email + "==========");
                    logger.info("=========" + email2 + "==========");
                    logger.info("==========================");
                    if (email != null) {
                        logger.info("---------------if----------");
                        activeUserService.removeUserGeneral(email);
                    } else {
                        logger.info("---------------else----------");
                    }
                    }
                }) // Responder con un 200 al cerrar sesión
            );

        return http.build();
    }

    // Definir configuración global de CORS
    //"/user/**","/rol/**","/assesment/**","/attendance/**","/calification/**","/group/**","/class/**","/message/**","/studentXParent/**","/subject/**","/userXGroup/**"
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
