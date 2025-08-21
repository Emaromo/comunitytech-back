/* Configuración de Axios para incluir token JWT automáticamente en cada petición
 */

import axios from "axios";
import { getToken } from "./localStorage";

// 🧱 Creamos una instancia personalizada de Axios
// Esto es lo que vas a usar en todo tu frontend para hacer peticiones HTTP al backend
const api = axios.create({
// 🌐 Dirección base de tu backend (Spring Boot)
// Cada vez que uses api.get("/algo"), esto se convierte en http://localhost:8082/algo
baseURL: "http://localhost:8082",
});


// 🎯 Este interceptor se ejecuta antes de cada petición (GET, POST, PUT, etc.)
// y permite modificar la configuración de la misma.
api.interceptors.request.use((config) => {
const token = getToken();

 // ✅ Si el token existe, lo agregamos al encabezado de la petición
  // Esto permite que el backend verifique si el usuario está autenticado
if (token) {
    config.headers.Authorization = "Bearer " + token;
}

  // 🔁 Devolvemos la configuración de la petición, ya modificada con el token (si existe)
return config;
});

export default api;