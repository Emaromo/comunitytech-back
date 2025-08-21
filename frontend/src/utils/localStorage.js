/**
 * Funciones para guardar, obtener y eliminar el token JWT en el almacenamiento local del navegador.
 * Esto permite persistir la sesión del usuario entre recargas o cierres del navegador.
 */
import { jwtDecode } from "jwt-decode";

/**
 * Extrae el email del usuario a partir del token JWT guardado.
 *
 * @returns {string|null} El email en minúsculas si se pudo decodificar, o null si el token es inválido o no existe.
 */
export function getUserEmail() {
const token = getToken();

if (!token) return null;

try {
    const decoded = jwtDecode(token); // Decodificamos el JWT
    //return decoded.email?.toLowerCase() || null; // ⬅️ Convertimos a minúsculas
    return decoded.sub?.toLowerCase() || null
} catch (error) {
    console.error("❌ Error al decodificar el token JWT:", error);
}
}

/**
 * Guarda el token JWT en localStorage bajo la clave "token".
 * @param {string} token - El token JWT recibido del backend tras login.
 */
export function saveToken(token) {
localStorage.setItem("token", token);
}

/**
 * Obtiene el token JWT guardado en localStorage.
 * @returns {string|null} El token JWT si existe, o null si no está guardado.
 */
export function getToken() {
return localStorage.getItem("token");
}

/**
 * Elimina el token JWT guardado en localStorage.
 * Se usa para "cerrar sesión" o limpiar tokens inválidos.
 */
export function clearToken() {
localStorage.removeItem("token");
}