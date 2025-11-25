package com.example.java.proyect.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permite enviar cookies, Authorization y JWT
        config.setAllowCredentials(true);

        // ==========================
        // 🌍 ORÍGENES PERMITIDOS
        // ==========================
        config.setAllowedOrigins(Arrays.asList(
            // 🔵 Entorno local
            "http://localhost:3000",

            // 🟣 IP del VPS (sin HTTPS)
            "http://66.97.42.236",
            "http://66.97.42.236:8080",
            "http://66.97.42.236:8082",

            // 🌐 Dominio de producción (frontend)
            "https://comunitytech.com.ar",
            "https://www.comunitytech.com.ar",
            

            // 🔥 Dominio del backend (IMPORTANTE!)
            "https://api.comunitytech.com.ar"
        ));

        // Permite todos los métodos HTTP
        config.addAllowedMethod("*");

        // Permite todos los headers
        config.addAllowedHeader("*");

        // URLs afectadas (toda la API)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
