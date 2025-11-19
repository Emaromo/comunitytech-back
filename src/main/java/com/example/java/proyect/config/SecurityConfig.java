package com.example.java.proyect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ⚙️ Configuración de seguridad de Spring Security
 * - Protege rutas con JWT
 * - Habilita CORS para frontend
 * - Permite ciertos caracteres especiales en URLs (como %0A)
 */
@Configuration
public class SecurityConfig {

    // 🔐 Codificador de contraseñas con BCrypt
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }

    /**
     * 🔐 Configuración del firewall para permitir ciertos caracteres codificados.
     * Soluciona errores como "%0A" en URLs rechazadas por Spring Security.
     */
    @Bean
    public HttpFirewall allowUrlEncodedHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();

        // ✅ Permite ciertos caracteres especiales en las URLs
        firewall.setAllowUrlEncodedPercent(true);        // %xx (ej: %0A)
        firewall.setAllowBackSlash(true);                // \
        firewall.setAllowUrlEncodedSlash(true);          // %2F
        firewall.setAllowUrlEncodedDoubleSlash(true);    // %2F%2F
        firewall.setAllowUrlEncodedPeriod(true);         // %2E
        firewall.setAllowSemicolon(true);                // ;

        return firewall;
    }

    // 📦 Aplica el firewall personalizado a todo el proyecto
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall firewall) {
        return (web) -> web.httpFirewall(firewall);
    }

    /**
     * 🔐 Cadena de filtros de seguridad
     * - Protege rutas con JWT
     * - Define qué rutas son públicas y cuáles requieren token
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 🌐 Habilita CORS (configurado más abajo)
            .cors(cors -> cors.configurationSource(request -> {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of(
                    "http://localhost:3000",           // 🧪 Frontend local
                    "https://comunitytech.com.ar"      // 🌐 Producción
                ));
                config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(java.util.List.of("*"));
                config.setAllowCredentials(true); // ✅ Importante para enviar cookies o headers de auth
                return config;
            }))

            // ❌ Desactiva CSRF (no usamos sesiones, solo JWT)
            .csrf(csrf -> csrf.disable())

            // 🛡️ Define acceso a rutas
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/", "/index", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                .requestMatchers("/test-email").permitAll()

                // Rutas que requieren login (JWT válido)
                .requestMatchers("/tickets/cliente/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()

                // Rutas solo para administradores
                .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_ADMIN")

                // Todo lo demás requiere estar autenticado
                .anyRequest().authenticated()
            )

            // 🚫 No usamos sesiones, solo JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ➕ Filtro JWT personalizado antes del filtro de autenticación por defecto
            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 🌍 Configuración global de CORS
     * Permite que el frontend se comunique con el backend desde dominios distintos
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                            "http://localhost:3000",        // Local
                            "https://comunitytech.com.ar"   // Producción
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Necesario para que funcione con JWT + cookies si se usan
            }
        };
    }
}
