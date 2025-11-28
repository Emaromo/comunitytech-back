package com.example.java.proyect.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS para permitir peticiones desde el frontend en producción
 * (comunitytech.com.ar) y desarrollo (localhost:5173).
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Permite CORS en todos los endpoints ("/**")
                registry.addMapping("/**")
                        // Orígenes permitidos: dominio de producción con HTTPS y localhost de desarrollo
                        .allowedOrigins("https://comunitytech.com.ar", "http://localhost:5173")
                        // Métodos HTTP permitidos (GET, POST, PUT, DELETE, etc.)
                        .allowedMethods("*")
                        // Headers permitidos (todas las cabeceras)
                        .allowedHeaders("*")
                        // Permitir envío de credenciales (por ejemplo, cookies o Authorization headers)
                        .allowCredentials(true);
            }
        };
    }
}
