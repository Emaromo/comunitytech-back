package com.example.java.proyect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 🔐 Configuración de seguridad para Spring Boot usando JWT.
 * - Permite login y registro sin autenticación.
 * - Desactiva CSRF (porque usamos JWT).
 * - Activa CORS usando CorsConfig.
 * - Aplica filtro JWT para proteger rutas privadas.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // ✅ Registramos el filtro JWT como bean para que Spring lo inyecte
    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() {
        return new JWTAuthorizationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {

        return http
            // ✅ CORS habilitado y CSRF deshabilitado usando la sintaxis moderna
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())

            // 🔐 Reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // ⚙️ Permitir preflight requests (CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 🔓 Rutas públicas: login y registro
                .requestMatchers(
                    "/users",            // Registro
                    "/users/login",      // Login
                    "/test-email",       // Otros endpoints públicos (si los usás)
                    "/auth/**", "/api/auth/**" // Opcional si tenés otras rutas de auth
                ).permitAll()

                // 🔐 Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )

            // 🔒 No usamos sesiones (stateless)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 📦 Aplicamos el filtro JWT antes del filtro de autenticación por usuario/contraseña
            .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)

            .build();
    }
}
