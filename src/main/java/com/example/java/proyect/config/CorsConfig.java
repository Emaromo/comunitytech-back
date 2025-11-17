package com.example.java.proyect.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuración global de CORS.
 * 
 * Permite:
 *  - Peticiones desde localhost (desarrollo)
 *  - Peticiones desde la IP del VPS (EasyPanel)
 *  - Peticiones desde dominio en producción
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        /**
         * ================================================================
         * 🌍 ORÍGENES PERMITIDOS
         * ================================================================
         * Dejamos tu origen original (localhost:3000) tal cual,
         * y agregamos los de producción.
         */
        config.setAllowedOrigins(Arrays.asList(
            // 🔵 Tu entorno local (ya te funcionaba)
            "http://localhost:3000",

            // 🟣 EasyPanel / IP del VPS
            "http://66.97.42.236",
            "http://66.97.42.236:8080",
            "http://66.97.42.236:8082",

            // 🌐 Dominio en producción (por si usás SSL)
            "https://comunitytech.com.ar",
            "http://comunitytech.com.ar"
        ));

        // Permite credenciales y tokens JWT
        config.setAllowCredentials(true);

        // Permite todos los headers
        config.addAllowedHeader("*");

        // Permite todos los métodos HTTP
        config.addAllowedMethod("*");

        // Aplica la configuración a toda la API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
