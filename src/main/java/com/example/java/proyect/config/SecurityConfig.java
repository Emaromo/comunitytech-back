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

@Configuration
public class SecurityConfig {

    /** 🔐 Codificador de contraseñas seguro (BCrypt) */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }

    /** 🚧 Firewall permisivo (permite caracteres codificados en URLs) */
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
        return web -> web.httpFirewall(firewall);
    }

    /** 🔐 Configuración principal de seguridad (filtros, rutas y JWT) */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // No usamos CSRF porque trabajamos con JWT

            .authorizeHttpRequests(auth -> auth

                /** 🔓 RUTAS PÚBLICAS (sin token requerido) */
                .requestMatchers(
                    "/", "/index",
                    "/swagger-ui.html", "/swagger-ui/**",
                    "/v3/api-docs/**", "/swagger-resources/**",

                    // 🚀 Login permitidos (todas las variantes)
                    "/login", "/api/login", "/users/login",

                    // 🚀 Registro de usuarios (todas las variantes)
                    HttpMethod.POST, "/users", "/api/users", "/users/**", "/api/users/**",

                    // 🚀 Endpoint opcional de test email
                    "/test-email"
                ).permitAll()

                /** 👤 RUTAS QUE REQUIEREN USUARIO AUTENTICADO */
                .requestMatchers("/tickets/cliente/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()

                /** 👑 RUTAS SOLO PARA ADMINISTRADORES */
                .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")

                /** 🔒 Cualquier otra ruta requiere autenticación */
                .anyRequest().authenticated()
            )

            /** ♻️ Modo sin sesiones → usamos solo JWT */
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            /** ⚙️ Aplicamos nuestro filtro JWT antes del default */
            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
