package com.example.java.proyect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  // Indica que esta clase provee configuraciones a la aplicación
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // Definimos un bean de configuración WebMvcConfigurer para configurar CORS globalmente
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Habilitamos CORS para todas las rutas de la API
                registry.addMapping("/**")
                    // Permitimos orígenes específicos en lugar de "*", ya que allowCredentials=true requiere orígenes explícitos
                    .allowedOrigins(
                        "http://localhost:3000",   // Origen del entorno de desarrollo (React, Angular u otro en puerto 3000)
                        "http://localhost:5173",   // Origen alternativo de desarrollo (por ejemplo Vite en puerto 5173)
                        "https://comunitytech.com.ar" // Origen de producción (dominio de la aplicación frontend en producción)
                    )
                    // Permitimos los métodos HTTP que usará nuestra API
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    // Permitimos los encabezados habituales que pueden enviar las solicitudes (Authorization para JWT, Content-Type, etc.)
                    .allowedHeaders("Authorization", "Content-Type", "Origin", "Accept", "X-Requested-With")
                    // Habilitamos el envío de credenciales (como cookies, cabeceras de autenticación). 
                    // Esto requiere orígenes explícitos (no se puede usar "*") y en el cliente hay que habilitar las credenciales.
                    .allowCredentials(true);
            }
        };
    }
}
