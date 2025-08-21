/**
 * Función para obtener el rol del usuario a partir del token JWT guardado en localStorage.
 * Esta función:
 * - Obtiene el token usando getToken().
 * - Verifica que el token tenga la estructura JWT (3 partes separadas por ".").
 * - Decodifica el token usando jwtDecode para extraer la información.
 * - Retorna el campo "role" dentro del token, o null si no existe o token inválido.
 * - Si el token es inválido o corrupto, lo elimina llamando clearToken().
 */

import { getToken, clearToken } from "./localStorage"; // Funciones para manejar el token
import { jwtDecode } from "jwt-decode";                // Librería para decodificar JWT

export function getUserRole() {
  // Obtenemos el token almacenado
  const token = getToken();

  // Si no hay token, no estamos logueados
  if (!token) return null;

  // Validamos que tenga formato JWT válido (3 partes separadas por ".")
  if (token.split(".").length !== 3) {
    console.warn("⚠️ Token inválido o mal formado:", token);
    clearToken(); // Limpiamos token corrupto
    return null;
  }

  try {
    // Decodificamos el token para acceder a sus datos
    const decoded = jwtDecode(token);

    // Si no contiene campo "role", limpiamos y retornamos null
    if (!decoded.role) {
      clearToken();
      return null;
    }

    // Retornamos el rol encontrado (ej. "ROLE_ADMIN", "ROLE_CLIENTE")
    return decoded.role;
  } catch (error) {
    // Si falla la decodificación (token corrupto o expirado), limpiamos y retornamos null
    console.error("❌ No se pudo decodificar el token:", error.message);
    clearToken();
    return null;
  }
}
export function logout() {
  clearToken();             // Eliminamos el token guardado
  window.location.reload(); // Recargamos la app para volver al login/registro
}