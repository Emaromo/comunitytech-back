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
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
public class SecurityConfig {

    // ==================================================
    // 🔐 ENCODER DE CONTRASEÑAS (BCrypt)
    // ==================================================
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return bCryptPasswordEncoder();
    }

    // ==================================================
    // 🔥 FIREWALL PERSONALIZADO (COMPATIBLE CON TU VERSIÓN)
    // ==================================================
    @Bean
    public HttpFirewall customHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();

        // 🌐 Permite caracteres codificados seguros
        firewall.setAllowUrlEncodedPercent(true);         // Permite %xx
        firewall.setAllowUrlEncodedSlash(true);           // Permite %2F
        firewall.setAllowUrlEncodedDoubleSlash(true);     // Permite //
        firewall.setAllowSemicolon(true);                 // Permite ;
        firewall.setAllowBackSlash(true);                 // Permite \
        firewall.setAllowUrlEncodedPeriod(true);          // Permite %2E

        return firewall;
    }

    // ==================================================
    // 🔰 CONFIGURACIÓN PRINCIPAL DE SEGURIDAD
    // ==================================================
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors()
            .and()
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()   // Preflight

                // === 📌 RUTAS PÚBLICAS ==
                .requestMatchers(HttpMethod.POST, "/users", "/users/login", "/api/users", "/api/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                .requestMatchers("/test-email", "/", "/index",
                                 "/swagger-ui.html", "/swagger-ui/**",
                                 "/v3/api-docs/**", "/swagger-resources/**").permitAll()

                // === 👤 CLIENTE AUTENTICADO ===
                .requestMatchers("/tickets/cliente/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()

                // === 👑 SOLO ADMIN ===
                .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")

                // 🔒 Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 🚨 Aplicamos el firewall personalizado (soluciona el %0A)
        http.setSharedObject(HttpFirewall.class, customHttpFirewall());

        return http.build();
    }
}
