package com.example.java.proyect.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // 🔐 Desactivamos CSRF porque usamos API REST + JWT (no sesiones)
            .csrf(csrf -> csrf.disable())

            // 🌍 Habilitamos CORS para permitir llamadas desde Postman, front, navegador, etc.
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .authorizeHttpRequests(auth -> auth
                // === 📌 Rutas públicas (sin token) ===
                .requestMatchers(HttpMethod.POST, "/users").permitAll()  // Crear usuario
                .requestMatchers(HttpMethod.POST, "/login", "/users/login").permitAll() // Login

                // Swagger y utilidades
                .requestMatchers("/test-email", "/", "/index",
                                 "/swagger-ui.html", "/swagger-ui/**",
                                 "/v3/api-docs/**", "/swagger-resources/**").permitAll()

                // === 🛡 Rutas protegidas: TOKEN requerido ===
                .requestMatchers("/tickets/cliente/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()

                // === 👑 Exclusivo ADMIN ===
                .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")

                // 🔒 Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )

            // 🚫 No usamos sesiones. Todo debe validarse con JWT.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 🚨 Registramos nuestro filtro JWT ANTES del filtro estándar de autenticación
            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ============================================
    // 🌐 CONFIGURACIÓN GLOBAL DE CORS
    // ============================================
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 🔓 Permitimos el dominio del Frontend y Postman
        config.setAllowedOrigins(List.of(
            "http://localhost:5173", // Tu front en desarrollo
            "http://localhost:3000",
            "https://api.comunitytech.com.ar",
            "https://comunitytech.com.ar",
            "*"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
