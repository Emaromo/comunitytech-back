package com.example.java.proyect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * 🔐 SecurityConfig
 *
 * Configura toda la seguridad de la aplicación:
 *  - Define qué rutas son públicas, autenticadas y de administrador.
 *  - Integra JWT (sin sesiones).
 *  - No maneja CORS aquí, porque está centralizado en CorsConfig.java.
 *  - TOTALMENTE COMPATIBLE con front local y producción.
 */

@Configuration
public class SecurityConfig {

    /** 1️⃣ — Codificador de contraseñas con BCrypt (encriptación segura) */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }

    /** 2️⃣ — Firewall permisivo para permitir rutas con caracteres especiales (evita errores %2F o %20) */
    @Bean
    public HttpFirewall allowUrlEncodedHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        firewall.setAllowUrlEncodedPeriod(true);
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall firewall) {
        return (web) -> web.httpFirewall(firewall);
    }

    /**
     * 3️⃣ — Cadena principal de seguridad:
     *  - No usamos sesiones → modo STATELESS (JWT)
     *  - Se desactiva CSRF
     *  - Se definen rutas públicas, autenticadas y de admin.
     *  - Se integra nuestro filtro JWT (JWTAuthorizationFilter).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            /** 🚫 1. Deshabilitar CSRF porque usamos JWT (no cookies, no formularios) */
            .csrf(csrf -> csrf.disable())

            /** 🛡️ 2. Configurar reglas de acceso por rutas */
            .authorizeHttpRequests(auth -> auth

                /** 🔓 RUTAS PÚBLICAS → no requieren token */
                .requestMatchers(
                    "/login",           // Login principal
                    "/api/login",       // Opción alternativa si algún endpoint usa /api
                    "/users",           // Registro
                    "/users/login",     // Si el login está mapeado así
                    "/test-email",      // Endpoint libre
                    "/", "/index",      // Entrada pública
                    "/swagger-ui.html", "/swagger-ui/**",
                    "/v3/api-docs/**", "/swagger-resources/**"
                ).permitAll()

                /** 👤 RUTAS QUE REQUIEREN USUARIO AUTENTICADO (ROLE_CLIENTE o ROLE_ADMIN) */
                .requestMatchers("/tickets/cliente/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()

                /** 👑 RUTAS EXCLUSIVAMENTE ADMINISTRADOR */
                .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")

                /** ⚠️ Cualquier otra ruta requiere autenticación */
                .anyRequest().authenticated()
            )

            /** ♻️ 3. Forzamos modo STATELESS (sin sesiones, solo JWT) */
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            /** 🔌 4. Agregamos nuestro filtro JWT antes del filtro de autenticación */
            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
