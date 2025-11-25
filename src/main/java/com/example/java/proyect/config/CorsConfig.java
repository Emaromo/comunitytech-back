package com.example.java.proyect.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    /**
     * 🌐 Filtro CORS global
     * Permite que el frontend pueda comunicarse con el backend desde otros orígenes (dominios)
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Habilita cookies y headers como Authorization
        config.setAllowCredentials(true);

        // ========================================
        // 🌍 ORÍGENES PERMITIDOS
        // ⚠️ Usa patrones para evitar error 403 en producción con HTTPS + cookies
        // ========================================
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:3000",              // 💻 Desarrollo local (React)
            "https://comunitytech.com.ar",        // 🌐 Producción
            "https://www.comunitytech.com.ar"     // 🌐 www también (en caso de usarlo)
        ));

        // ✅ Métodos HTTP permitidos
        config.addAllowedMethod("*");

        // ✅ Headers permitidos
        config.addAllowedHeader("*");

        // ✅ Aplica a toda la API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
