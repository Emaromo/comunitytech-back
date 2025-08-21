// Página de login y registro con fondo futurista, título animado y alternancia visual entre ambos formularios.

import React, { useState } from "react";
import Login from "../component/login/Login";       // Componente de login
import Signup from "../component/signup/Signup";   // Componente de registro
import { useNavigate } from "react-router-dom";    // Hook para navegación
import { getUserRole } from "../utils/auth";       // Función para obtener rol desde el token
import Footer from "../component/Footer";

export default function LoginSignupPage() {
  const [showLogin, setShowLogin] = useState(true);  // Estado para mostrar Login o Registro
  const navigate = useNavigate();                    // Permite redirigir según el rol

  /**
   * Función que se ejecuta después de un login exitoso
   * Redirige al dashboard según el rol del usuario
   */
  const handleLoginSuccess = () => {
    const role = getUserRole();
    if (role === "ROLE_ADMIN") navigate("/admin");
    else if (role === "ROLE_CLIENTE") navigate("/client");
  };

  return (
    // Contenedor principal que ocupa toda la pantalla (alto mínimo 100vh)
    // Flex columna para ordenar main y footer verticalmente
    // Sin justify-between para que no se estiren hacia extremos
    <div
      className="min-h-screen flex flex-col bg-cover bg-center"
      style={{ backgroundImage: `url('/fondo-tech4.jpg')` }}
    >
      {/*
        Main ocupa todo el espacio entre header y footer gracias a flex-grow
        Es flex para centrar contenido vertical y horizontalmente
      */}
      <main className="flex-grow flex justify-center items-center h-full">
        {/*
          Contenedor interno:
          - Ocupa toda la altura de main (h-full)
          - Flex columna, contenido centrado vertical y horizontalmente
          - Ancho máximo definido para que no se estire demasiado
          - Fondo negro semi-transparente
          - Padding y sombra
        */}
        <div className="bg-black bg-opacity-60 w-full max-w-md h-full flex flex-col justify-start items-center p-6 rounded-none shadow-lg">
          <h1 className="text-white text-3xl font-bold text-center mb-1">UNITY TECH</h1>
          <p className="text-gray-400 text-center text-sm mb-6">COMPUTERS SERVICE</p>

          <div className="flex gap-4 mb-6 w-full">
            <button
              onClick={() => setShowLogin(true)}
              className={`px-6 py-2 rounded font-semibold transition-all w-1/2 text-white duration-300 ${
                showLogin
                  ? "bg-gradient-to-r from-blue-700 to-black shadow-lg shadow-blue-500/40"
                  : "bg-zinc-950 bg-opacity-190 hover:bg-zinc-900 text-black-400"
              }`}
            >
              Login
            </button>
            <button
              onClick={() => setShowLogin(false)}
              className={`px-6 py-2 rounded font-semibold transition-all w-1/2 text-white duration-300 ${
                !showLogin
                  ? "bg-gradient-to-r from-blue-700 to-black shadow-lg shadow-blue-500/40"
                  : "bg-zinc-950 bg-opacity-190 hover:bg-zinc-900 text-gray-400"
              }`}
            >
              Signup
            </button>
          </div>

          {/* 
            Se muestra el componente Login o Signup según el estado.
            Si es Login, le pasamos la función handleLoginSuccess como prop para que se ejecute desde adentro cuando sea exitoso.
          */}
          <div className="w-full">
            {showLogin ? (
              <Login onLoginSuccess={handleLoginSuccess} />
            ) : (
              <Signup />
            )}
          </div>
        </div>
      </main>

      {/* Footer pegado abajo */}
      <Footer />
    </div>
  );
}