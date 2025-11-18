package com.example.java.proyect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de seguridad:
 * - JWT
 * - Rutas protegidas vs públicas
 * - CORS para frontend local y producción
 */
@Configuration
public class SecurityConfig {

    // 🔐 Codificador de contraseñas (BCrypt)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }

    // 🔐 Configuración principal de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 🌐 CORS habilitado para frontend local y producción
            .cors(cors -> cors.configurationSource(request -> {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of(
                    "http://localhost:3000",           // 🧪 Frontend local
                    "https://comunitytech.com.ar"      // 🌐 Producción
                ));
                config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(java.util.List.of("*"));
                config.setAllowCredentials(true); // ✅ Si usás cookies o headers de autenticación
                return config;
            }))

            // 🚫 Desactivar CSRF (usamos JWT, no sesiones)
            .csrf(csrf -> csrf.disable())

            // 🔐 Reglas de acceso a rutas
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/", "/index", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                .requestMatchers("/test-email").permitAll()

                // Rutas autenticadas
                .requestMatchers("/tickets/cliente/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()

                // Rutas solo para admin
                .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_ADMIN")

                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )

            // 🚫 No usar sesiones: cada request debe tener JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ➕ Agregar filtro JWT personalizado antes del filtro por defecto
            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🌐 Configuración global para permitir CORS desde frontend local y producción
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                            "http://localhost:3000",        // 🧪 Desarrollo local
                            "https://comunitytech.com.ar"   // 🌐 Producción
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
