package com.example.java.proyect.config;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * 🔐 Clase utilitaria para crear y validar JWTs.
 * Usa la versión 0.11.5 de jjwt (compatible con Java 17+).
 */
public class JWTUtil {

    // 🔐 Devuelve la clave secreta decodificada desde BASE64
    private static Key getSigningKey() {
        // 1️⃣ Decodificamos la clave secreta en BASE64
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.TOKEN_SECRET);
        // 2️⃣ Creamos la clave HMAC segura
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 🛠️ Genera un token firmado con subject (email) y claim (rol).
     */
    public static String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email) // 📧 Quién es el dueño del token
                .claim("role", role) // 🎭 Agregamos el rol como claim adicional
                .setIssuedAt(new Date()) // 📅 Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE)) // 📅 Expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // 🖊️ Firmamos con HS512
                .compact(); // 🎫 Generamos el token (String)
    }

    /**
     * 📄 Parsea el token y devuelve sus claims (payload).
     */
    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX + " ", "")) // Quitamos "Bearer "
                .getBody();
    }

    /**
     * 📧 Devuelve el email (subject) guardado en el token.
     */
    public static String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 🎭 Devuelve el rol guardado en el token.
     */
    public static String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    /**
     * ✅ Verifica que el token sea válido (firma y vencimiento).
     */
    public static boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false; // Token inválido
        }
    }
}