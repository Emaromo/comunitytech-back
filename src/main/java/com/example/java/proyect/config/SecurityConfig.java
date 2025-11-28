package com.example.java.proyect.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security con JWT.
 * Se deshabilita CSRF (ya que usamos JWT), se configura CORS y se registra el filtro JWT.
 * Se permiten sin autenticación las rutas de login y registro, y las peticiones OPTIONS (CORS preflight).
 * Nota: Si despliegas con EasyPanel (Docker), asegúrate de configurar correctamente el "Proxy Port"
 * (puerto interno del contenedor, e.g. 8080) para que tu dominio apunte al puerto correcto de tu app.
 * Las cabeceras CORS son manejadas por esta configuración, por lo que no se requiere ajustar nada en el proxy.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Habilitar CORS con la configuración global (CorsConfig) y desactivar CSRF
        http.cors().and().csrf().disable();

        // Configurar las políticas de acceso
        http.authorizeHttpRequests(auth -> auth
            // Permitir todas las peticiones OPTIONS (preflight de CORS) sin autenticación
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // Permitir sin autenticación los endpoints públicos de autenticación (login y registro)
            .requestMatchers("/auth/**", "/api/auth/**").permitAll() // ajustar rutas según tu API
            // Cualquier otra petición requiere autenticación
            .anyRequest().authenticated()
        );

        // Configurar sesión: sin estado (no se usarán sesiones HTTP)
        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Registrar el filtro JWT antes del filtro de autenticación por contraseña de Spring
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
