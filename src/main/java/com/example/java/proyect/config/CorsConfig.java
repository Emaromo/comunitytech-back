package com.example.java.proyect.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuración global de CORS para la aplicación.
 * 
 * Esta clase crea un filtro que permite que el backend acepte peticiones
 * desde el frontend en http://localhost:3001 (tu React app).
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // Creamos una configuración CORS
        CorsConfiguration config = new CorsConfiguration();

        // Permitimos peticiones solo desde este origen (tu frontend React)
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));

        // Permitimos que se envíen credenciales (cookies, headers Authorization, etc)
        config.setAllowCredentials(true);

        // Permitimos todos los headers (Content-Type, Authorization, etc)
        config.addAllowedHeader("*");

        // Permitimos todos los métodos HTTP (GET, POST, PUT, DELETE, OPTIONS, etc)
        config.addAllowedMethod("*");

        // Fuente que aplicará la configuración a todas las rutas ("**")
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Registramos la configuración para todas las rutas de la API
        source.registerCorsConfiguration("/**", config);

        // Retornamos un filtro que aplica esta configuración en cada petición
        return new CorsFilter(source);
    }
}