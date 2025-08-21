// Importamos los íconos necesarios desde lucide-react
import { UserCog, LogOut } from "lucide-react";

// Importamos React y hooks necesarios
import React, { useState, useEffect } from "react";

// Importamos la función de logout que limpia el localStorage y redirige
import { logout } from "../../utils/auth";

// Axios personalizado para enviar peticiones con el token JWT
import api from "../../utils/axiosConfig";

/**
 * 🧠 Función utilitaria para poner la primera letra en mayúscula
 * y el resto en minúscula.
 * Ej: "emanuel" -> "Emanuel"
 */
const capitalize = (str) => {
  if (!str) return "";
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
};

/**
 * 💼 Componente que muestra una tarjeta con:
 * - Un mensaje de bienvenida personalizado usando nombre y apellido.
 * - Un botón para cerrar sesión.
 * 
 * Recibe como prop el `email` que se usa para buscar los datos del usuario en el backend.
 */
export default function TechnicalCard({ email }) {
  // 🎯 Estado para guardar el nombre y apellido del usuario (desde backend)
  const [userName, setUserName] = useState({ firstName: "", lastName: "" });

  // ⚠️ Estado para capturar errores al hacer la petición
  const [error, setError] = useState(null);

  /**
   * 🧩 useEffect que se ejecuta al cargar el componente o si cambia el email.
   * Hace una petición al backend para traer los datos del usuario por email.
   */
  useEffect(() => {
    if (!email) return; // Si no tenemos email, salimos sin hacer nada.

    const fetchUserData = async () => {
      try {
        // 🔎 GET a backend: trae el usuario por email
        const response = await api.get(`/users/email/${email}`);

        // ✅ Si fue exitoso, extraemos el nombre y apellido y lo guardamos en el estado
        if (response.status === 200) {
          const { firstName, lastName } = response.data;

          // Guardamos en estado aplicando formato capitalizado
          setUserName({
            firstName: capitalize(firstName),
            lastName: capitalize(lastName),
          });

          setError(null); // Limpiamos error
        }
      } catch (err) {
        // ❌ Si falla la petición, mostramos error y vaciamos los datos
        console.error(err);
        setError("No se pudo cargar el nombre del usuario.");
        setUserName({ firstName: "", lastName: "" });
      }
    };

    fetchUserData(); // Ejecutamos la función async
  }, [email]); // Dependencia: se vuelve a ejecutar si cambia el email

  return (
    <div className="w-full bg-black border-2 border-blue-700 rounded-lg px-6 py-4 flex items-center justify-between transition duration-300 hover:shadow-[0_0_10px_rgba(59,130,246,0.9)]">
      
      {/* 📌 Sección izquierda: icono + mensaje de bienvenida */}
      <div className="flex items-center gap-3">
        {/* Icono de usuario */}
        <div className="p-2 bg-zinc-800 rounded-full">
          <UserCog className="w-6 h-6 text-blue-400" />
        </div>

        {/* Texto de bienvenida dinámico */}
        <h2 className="text-white text-lg font-bold">
          {/* 🔄 Mostramos mensaje según estado */}
          {error
            ? "Bienvenido.."
            : userName.firstName && userName.lastName
            ? `Bienvenido ${userName.firstName} ${userName.lastName}...`
            : "Bienvenido.."}
        </h2>
      </div>

      {/* 📌 Sección derecha: botón de cerrar sesión */}
      <button
        onClick={logout}
        className="flex items-center gap-2 px-4 py-2 rounded-lg bg-gradient-to-r from-blue-700 to-black text-white font-medium text-sm shadow-md hover:shadow-blue-500/40 hover:scale-105 transition-all duration-300"
      >
        <LogOut className="w-4 h-4" />
        Cerrar sesión
      </button>
    </div>
  );
}