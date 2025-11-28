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
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",            // Frontend local
            "http://localhost:5173",            // Vite local
            "https://comunitytech.com.ar",      // Producción
            "https://www.comunitytech.com.ar"   // Variante www
        ));

        // 📡 Métodos HTTP aceptados
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 📦 Headers aceptados en la solicitud
        config.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin"
        ));

        // 🎁 Headers expuestos en la respuesta
        config.setExposedHeaders(List.of(
            "Authorization",
            "Content-Type"
        ));

        // 🚀 Aplicar a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
