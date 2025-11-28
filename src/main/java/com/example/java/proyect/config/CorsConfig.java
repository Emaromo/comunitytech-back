package com.example.java.proyect.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 🌐 CONFIGURACIÓN GLOBAL DE CORS PARA SPRING BOOT
 * ------------------------------------------------
 * - Permite que el frontend (local y deployado) se conecte al backend.
 * - Mantiene SecurityConfig separado y limpio.
 * - Compatible con JWT y cookies (con allowCredentials=true).
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 🔑 Permite credenciales (Authorization, cookies, JWT)
        config.setAllowCredentials(true);

        // 🌍 Dominios permitidos (local + producción)
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:3000",            // Frontend local (React)
            "http://localhost:5173",            // Vite o React alternativo
            "https://comunitytech.com.ar",      // Dominio principal
            "https://www.comunitytech.com.ar"   // Variante con www
        ));

        // 📡 Métodos HTTP aceptados
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 📦 Headers aceptados
        config.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin"
        ));

        // 🎁 Headers que el frontend podrá leer como respuesta
        config.setExposedHeaders(List.of(
            "Authorization",
            "Content-Type"
        ));

        // 🚀 Aplicar a TODAS las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
