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
            .cors() // ⬅️ ACTIVAMOS CORS PARA QUE USE CorsConfig
            .and()
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // 💡 IMPORTANTE → Permitir OPTIONS para preflight (CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // === 📌 RUTAS PÚBLICAS ===
                .requestMatchers(HttpMethod.POST, "/users", "/users/login", "/api/users", "/api/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()

                .requestMatchers("/test-email", "/", "/index",
                                 "/swagger-ui.html", "/swagger-ui/**",
                                 "/v3/api-docs/**", "/swagger-resources/**").permitAll()

                // === 👤 CLIENTE AUTENTICADO ===
                .requestMatchers("/tickets/cliente/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/tickets/*/notificacion").authenticated()

                // === 👑 ADMIN ===
                .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_ADMIN")

                // 🔒 Todo lo demás autenticado
                .anyRequest().authenticated()
            )

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
