package com.example.java.proyect.config;

/**
 * 🔐 Constantes utilizadas para la seguridad y autenticación JWT.
 */
public class SecurityConstants {

    // ⏳ Tiempo de expiración del token JWT (10 días en milisegundos)
    public static final long EXPIRATION_DATE = 864_000_000;

    // 📌 Prefijo estándar en el header Authorization (Bearer <token>)
    public static final String TOKEN_PREFIX = "Bearer";

    // 📌 Nombre del encabezado HTTP que contiene el token
    public static final String HEADER_STRING = "Authorization";

    // 🚪 Ruta pública permitida sin autenticación (registro)
    public static final String SIGN_UP_URL = "/users";

    // 🛡️ Clave secreta para firmar/verificar JWT (>= 64 bytes codificados en base64 para HS512)
    // ⚠️ ESTA ES UNA NUEVA CLAVE SEGURA, GENERADA CON Keys.secretKeyFor(SignatureAlgorithm.HS512)
    public static final String TOKEN_SECRET = "PUFFZg4uNDdh0Y6zDF8hRDvJqvYQ1rqU2YtxgfGHYr/O7hzfYkaTpruPCvEO2NRKCR+xwbzAeHKXMXNp1DN3djA==";
}