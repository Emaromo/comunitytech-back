import React, { useState } from "react";
import { saveToken } from "../../utils/localStorage"; // (Si usás token luego del registro)

/**
 * Componente de Registro de Usuario
 * Permite a un nuevo usuario ingresar sus datos y registrarse.
 */
export default function Signup() {
  // Estados para los inputs del formulario
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [message, setMessage] = useState(""); // Para mostrar mensajes de éxito o error

  /**
   * Función que maneja el envío del formulario
   */
  const handleSubmit = async (e) => {
    e.preventDefault(); // Prevenir comportamiento por defecto del formulario

    try {
      const res = await fetch("http://localhost:8082/users", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email,
          password,
          firstName,
          lastName,
        }),
      });

      if (res.ok) {
        setMessage("✅ Usuario registrado correctamente. Ya puedes iniciar sesión.");
        setTimeout(() => {
    setMessage(""); // Limpia el mensaje
  }, 3000)
      } else {
        setMessage("❌ Error al registrar usuario. El correo puede estar en uso.");
      }
    } catch (err) {
      console.error("❌ Error en la conexión:", err);
      setMessage("❌ Error de red o conexión con el servidor.");
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="space-y-5"
    >
      {/* Título del formulario */}
      <h2 className="text-2xl font-bold mb-1 text-center text-white tracking-wide">
      </h2>

      {/* Campo: Nombre */}
      <input
        type="text"
        placeholder="Nombre"
        value={firstName}
        onChange={(e) => setFirstName(e.target.value)}
        className="w-full p-2 bg-[#0f0f0f] border border-grey-900 text-white placeholder-gray-400 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        required
      />

      {/* Campo: Apellido */}
      <input
        type="text"
        placeholder="Apellido"
        value={lastName}
        onChange={(e) => setLastName(e.target.value)}
        className="w-full p-2 bg-[#0f0f0f] border border-grey-900 text-white placeholder-gray-400  rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        required
      />

      {/* Campo: Correo electrónico */}
      <input
        type="email"
        placeholder="Correo electrónico"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className="w-full p-2 bg-[#0f0f0f] border border-grey-900 text-white placeholder-gray-400  rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        required
      />

      {/* Campo: Contraseña */}
      <input
        type="password"
        placeholder="Contraseña (mín. 8 caracteres)"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className="w-full p-2 bg-[#0f0f0f] border border-grey-900 text-white placeholder-gray-400  rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        required
      />

      {/* Mensaje de estado (éxito o error) */}
      {message && (
        <p className="text-sm text-center mb-4 text-white bg-black bg-opacity-30 p-2 rounded">
          {message}
        </p>
      )}

      {/* Botón de registro */}
      <button
        type="submit"
        className="w-full
  bg-gradient-to-r from-gray-900 to-blue-900
  text-white
  font-semibold
  py-3 px-7
  rounded-lg
  border-2 border-blue-300
  shadow-[0_0_10px_#2563eb]
  transition
  duration-300
  hover:border-blue-400
  hover:shadow-[0_0_20px_#3b82f6]
  hover:scale-104
  "
  
>
      
        Signup
      </button>
    </form>
  );
}