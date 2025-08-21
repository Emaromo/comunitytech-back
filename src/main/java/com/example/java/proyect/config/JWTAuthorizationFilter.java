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
 * Este filtro se ejecuta una sola vez por petición (OncePerRequestFilter)
 * y valida el token JWT incluido en el encabezado Authorization.
 * Si es válido, extrae los datos del usuario y los inyecta en el contexto de seguridad de Spring.
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
                                    throws ServletException, IOException {

        // ⛔ Excluir rutas públicas (registro y login) del filtro
        String path = request.getServletPath();
        if (path.equals("/users") || path.equals("/users/login") || path.equals("/test-email")) {
    filterChain.doFilter(request, response);
    return;
}

        // 1️⃣ Obtenemos el header Authorization
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        // 2️⃣ Si no tiene token o no empieza con "Bearer ", dejamos pasar sin autenticar
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX + " ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3️⃣ Extraemos el token (sacamos "Bearer ")
        String token = header.replace(SecurityConstants.TOKEN_PREFIX + " ", "");

        // 4️⃣ Verificamos si el token es válido
        if (JWTUtil.isTokenValid(token)) {
            // 5️⃣ Extraemos los claims (datos del token)
            Claims claims = JWTUtil.getClaims(token);
            String email = claims.getSubject(); // Usuario autenticado
            String role = claims.get("role", String.class); // Rol: ROLE_CLIENTE o ROLE_ADMIN

            // 6️⃣ Creamos el objeto de autenticación con ese email y rol
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );

            // 7️⃣ Lo seteamos en el contexto de seguridad de Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 8️⃣ Continuamos con el resto del filtro o endpoint
        filterChain.doFilter(request, response);
    }
}
