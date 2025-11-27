package com.example.java.proyect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Bean para encriptar contraseñas
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
            // 🚫 Desactivamos CSRF para API REST (usamos tokens, no sesiones)
            .csrf(csrf -> csrf.disable())

            // 🔑 Autorización de rutas
            .authorizeHttpRequests(auth -> auth

                // === 📌 RUTAS PÚBLICAS (sin autenticación) ===
                .requestMatchers(HttpMethod.POST, "/users/**", "/api/users/**").permitAll()   // Registro de usuario
                .requestMatchers(HttpMethod.POST, "/login", "/api/login", "/users/login").permitAll() // Login público

                // Swagger y documentación pública
                .requestMatchers(
                    "/", "/index", "/error",
                    "/test-email",
                    "/swagger-ui.html", "/swagger-ui/**",
                    "/v3/api-docs/**", "/swagger-resources/**"
                ).permitAll()

                // === 👤 RUTAS QUE REQUIEREN TOKEN (cualquier usuario válido) ===
                .requestMatchers("/tickets/cliente/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()

                // === 👑 RUTAS SOLO ADMIN ===
                .requestMatchers(HttpMethod.POST, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")

                // 🔒 Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )

            // 🛑 No usamos sesiones, solo JWT (STATELESS)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 🔐 Filtro de JWT antes del filtro estándar de Spring Security
            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
