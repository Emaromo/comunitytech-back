// ✅ Componente Login.jsx
// Este componente permite al usuario ingresar con su email y contraseña.
// Si el backend valida las credenciales, se guarda el token JWT y se llama a la función onLoginSuccess.

import React, { useState } from "react";
import { saveToken } from "../../utils/localStorage"; // Función personalizada que guarda el token en localStorage
import { jwtDecode } from "jwt-decode";               // Utilidad para decodificar el JWT

export default function Login({ onLoginSuccess }) {
  // Estados para capturar lo que el usuario escribe
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // Estado para mostrar errores o mensajes informativos
  const [message, setMessage] = useState("");

  /**
   * Función que maneja el envío del formulario.
   * Hace la petición POST al backend con las credenciales.
   */
  const handleSubmit = async (e) => {
    e.preventDefault(); // Evita recargar la página

    try {
      const response = await fetch("http://localhost:8082/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const token = await response.text();       // JWT que devuelve el backend
        saveToken(token);                          // Lo guardamos en localStorage
        const decoded = jwtDecode(token);          // Decodificamos el token
        const role = decoded.role;                 // Extraemos el rol

        if (role) {
          onLoginSuccess();                        // Llamamos al padre para redireccionar
        } else {
          setMessage("Rol no reconocido en el token recibido.");
        }
      } else {
        setMessage("Credenciales inválidas, intenta nuevamente.");
      }
    } catch (error) {
      console.error("Error al hacer login:", error);
      setMessage("Error en la conexión con el servidor. Intenta más tarde.");
    }
  };

  // Render del formulario
  return (
    <form onSubmit={handleSubmit} className="space-y-5">
      {/* ✅ Campo de Email */}
      <input
        type="email"
        placeholder="Email Address"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
        className="w-full p-2 bg-[#0f0f0f] border border-grey-900 text-white placeholder-gray-400 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
      />

      {/* ✅ Campo de Contraseña */}
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
        className="w-full p-2 bg-[#0f0f0f] border border-grey-900 text-white placeholder-gray-400 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
      />

      {/* 🔵 Botón de inicio de sesión */}
      <button
        type="submit"
        className="w-full
  bg-gradient-to-r from-gray-900 to-blue-900
  text-white
  font-semibold
  py-3 px-7
  rounded-lg
  border-2 border-blue-10
  
  shadow-[0_0_5px_#2563eb]
  transition
  duration-300
  hover:border-blue-400
  hover:shadow-[0_0_20px_#3b82f6]
  hover:scale-104"
      >
        Login
      </button>

      {/* ❗Mensaje de error si existe */}
      {message && (
        <p className="text-sm text-center text-red-400 mt-2">{message}</p>
      )}

      {/* 🔗 Enlaces secundarios (opcional) */}
      <div className="mt-4 text-sm flex justify-center text-gray-400">
  <span>¿Olvidaste tu contraseña?</span>
</div>
    </form>
  );
}