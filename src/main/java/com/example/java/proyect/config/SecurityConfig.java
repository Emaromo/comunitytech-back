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
 * Configuración principal de seguridad para la aplicación.
 * Aquí se definen:
 * - Codificador de contraseñas (BCrypt)
 * - Reglas de acceso a endpoints según roles
 * - CORS habilitado para React
 * - Agregado del filtro JWT para validar cada petición
 */
@Configuration
public class SecurityConfig {

    // 🔐 Bean para codificar las contraseñas de forma segura
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean que devuelve el encoder para usar en servicios
    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad para HTTP.
     * - CORS habilitado solo para localhost:3000 (React)
     * - CSRF deshabilitado (usamos JWT)
     * - Sin sesión (stateless)
     * - Define quién puede acceder a qué rutas
     * - Añade el filtro JWT para validar token en cada request
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1️⃣ Configuración CORS para aceptar peticiones desde React
            .cors(cors -> cors.configurationSource(request -> {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of("http://localhost:3000"));
                config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE"));
                config.setAllowedHeaders(java.util.List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))

            // 2️⃣ Desactivar CSRF porque usamos JWT y no sesiones
            .csrf(csrf -> csrf.disable())

            // 3️⃣ Definir reglas de acceso por ruta y rol
          .authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.POST, "/users").permitAll()
    .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
    .requestMatchers("/test-email").permitAll()

    
    // Rutas para cliente autenticado
    .requestMatchers("/tickets/cliente/**").authenticated()
    .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()
    
    // Solo admin puede editar/eliminar/crear tickets
    .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
    .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")
    .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_ADMIN")

    // Todo lo demás necesita estar autenticado
    .anyRequest().authenticated()
)
    


            // 4️⃣ No se usan sesiones, cada request se valida con token
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 5️⃣ Agregar el filtro JWT para validar token antes que el filtro de autenticación por defecto
            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuración global para CORS (permite al navegador aceptar solicitudes desde React)
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // Solo React frontend
                        .allowedMethods("*") // Todos los métodos permitidos
                        .allowedHeaders("*") // Todos los headers, incluido Authorization
                        .allowCredentials(true);
            }
        };
    }
}