package com.example.java.proyect.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 🔐 Filtro de autorización JWT
 *
 * Este filtro se ejecuta en **cada petición** (OncePerRequestFilter) y valida si:
 *  1️⃣ La ruta requiere autenticación
 *  2️⃣ El token JWT es válido
 *  3️⃣ Extrae el email y rol del usuario desde el token
 *  4️⃣ Inserta la autenticación en el contexto de Spring Security
 *
 * Si la ruta es pública (login, registro, test-email), NO aplica validación JWT.
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Obtener la ruta solicitada
        String path = request.getServletPath();

        /**
         * 🔓 Rutas públicas (NO deben requerir token JWT)
         * - /login            → iniciar sesión
         * - /users            → registrar usuario
         * - /test-email       → pruebas de email
         * - /api/login        → login alternativo si se usa /api
         * - /users/login      → variante del login
         */
        if (
            path.equals("/login") ||
            path.equals("/api/login") ||
            path.equals("/users") ||
            path.equals("/users/login") ||
            path.equals("/test-email")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ Leer el encabezado Authorization
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        // ❌ Si NO hay token o no empieza con "Bearer ", dejamos que siga SIN autenticar
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX + " ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3️⃣ Extraer el token quitando el prefijo "Bearer "
        String token = header.replace(SecurityConstants.TOKEN_PREFIX + " ", "");

        // 4️⃣ Validar el token (firma + expiración)
        if (JWTUtil.isTokenValid(token)) {
            // 5️⃣ Extraer los claims (datos del token)
            Claims claims = JWTUtil.getClaims(token);
            String email = claims.getSubject(); // 📧 Email del usuario
            String role = claims.get("role", String.class); // 🎭 ROLE_CLIENTE o ROLE_ADMIN

            // 6️⃣ Crear objeto de autenticación con email y rol
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(role))
                    );

            // 7️⃣ Establecer autenticación en el contexto de Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 8️⃣ Continuar con el resto del filtro o endpoint
        filterChain.doFilter(request, response);
    }
}
