// ✅ Importamos React y useState para manejar los estados del formulario
import React, { useState } from "react";

// ✅ Importamos Axios configurado con el token JWT automáticamente
import api from "../../utils/axiosConfig";

/**
 * 🎯 Componente que permite al administrador crear un nuevo ticket.
 *
 * Recibe opcionalmente una función `onTicketCreated` que se ejecuta cuando el ticket se crea correctamente.
 */
export default function TicketForm({ onTicketCreated }) {
  // 📧 Correo del cliente
  const [clienteEmail, setClienteEmail] = useState("");
  // 🛠 Descripción del problema
  const [descripcionProblema, setDescripcionProblema] = useState("");
  // 🔄 Estado del ticket
  const [estado, setEstado] = useState("Pendiente");
  // ✅ Solución del técnico (opcional)
  const [solucion, setSolucion] = useState("");
  // 💲 Precio del servicio
  const [precio, setPrecio] = useState("");
  // 🔥 Prioridad del ticket
  const [prioridad, setPrioridad] = useState("BAJA");
  // 📩 Mensaje de éxito o error
  const [mensaje, setMensaje] = useState("");

  /**
   * 📬 Envía los datos del formulario al backend para crear un nuevo ticket.
   */
  const handleSubmit = async (e) => {
    e.preventDefault(); // ✋ Evita recargar la página

    try {
      // 🔁 Enviamos los datos al backend usando Axios
      const res = await api.post("/tickets", {
        clienteEmail,
        descripcionProblema,
        estado,
        solucion,
        precio,
        prioridad,
      });

      // 🎉 Si se creó correctamente
      if (res.status === 200 || res.status === 201) {
        setMensaje("✅ Ticket creado exitosamente.");
        onTicketCreated && onTicketCreated(res.data); // Ejecuta callback si viene por props
        setTimeout(() => {
        setMensaje(""); // Limpia el mensaje
        }, 3000);
        // 🔄 Limpiar los campos
        setClienteEmail("");
        setDescripcionProblema("");
        setEstado("Pendiente");
        setSolucion("");
        setPrecio("");
        setPrioridad("BAJA");
      }
    } catch (err) {
      console.error("❌ Error al crear ticket:", err);
      setMensaje("❌ No se pudo crear el ticket.");
    }
  };

  // 🎨 Formulario con diseño oscuro y elegante, manteniendo todos los comentarios explicativos
  return (
    <form
      onSubmit={handleSubmit}
      className="p-6 rounded-xbg-black-300l shadow-lg text-white space-y-8"
    >
      {/* 🧾 Título del formulario */}
      <h2 className="text-2xl font-bold mb-6 text-center text-white">
        Crear nuevo Ticket
      </h2>

      {/* 📧 Email del cliente */}
      <input
        type="email"
        placeholder="Correo del cliente"
        value={clienteEmail}
        onChange={(e) => setClienteEmail(e.target.value)}
        required
        className="w-full p-3 mb-4 bg-neutral-900 text-white border border-blue-800 rounded-lg focus:outline-none focus:ring-2 focus: focus:ring-blue-500"
      />

      {/* 🛠 Descripción del problema */}
      <textarea
        placeholder="Descripción del problema"
        value={descripcionProblema}
        onChange={(e) => setDescripcionProblema(e.target.value)}
        required
        className="w-full p-3 mb-4 bg-neutral-900 text-white border border-blue-800 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-blue-500"
      />

      {/* 🔄 Estado */}
      <select
        value={estado}
        onChange={(e) => setEstado(e.target.value)}
        className="w-full p-3 mb-4 bg-neutral-900 text-white border border-blue-800 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        <option value="Pendiente">Pendiente</option>
        <option value="En Reparación">En Reparación</option>
        <option value="Listo">Listo</option>
      </select>

      {/* ✅ Solución del técnico (opcional) */}
      <textarea
        placeholder="Solución (opcional)"
        value={solucion}
        onChange={(e) => setSolucion(e.target.value)}
        className="w-full p-3 mb-4 bg-neutral-900 text-white border border-blue-800 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-green-400"
      />

      {/* 💲 Precio */}
      <input
        type="number"
        placeholder="Precio"
        value={precio}
        onChange={(e) => setPrecio(e.target.value)}
        className="w-full p-3 mb-4 bg-neutral-900 text-white border border-blue-800 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
      />

      {/* 🔥 Prioridad */}
      <select
        value={prioridad}
        onChange={(e) => setPrioridad(e.target.value)}
        className="w-full p-3 mb-4 bg-neutral-900 text-white border border-blue-800 rounded-lg focus:outline-none focus:ring-2 focus:ring-yellow-400"
      >
        <option value="ALTA">Alta (rojo)</option>
        <option value="MEDIA">Media (naranja)</option>
        <option value="BAJA">Baja (amarillo)</option>
        <option value="REPARADO">Reparado (verde)</option>
      </select>

    
      {/* 🎯 Botón para enviar el formulario */}
      <button
        type="submit"
        className="w-full bg-gradient-to-r from-blue-700 to-black text-white p-3 rounded-lg font-semibold shadow-md hover:shadow-blue-500/40 transition-colors duration-200"
      >
        Crear Ticket
      </button>
      {/* ✅ Mensaje flotante sin scroll (toast interno) */}
      {mensaje && (
      <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 bg-zinc-800 text-white text-sm px-4 py-2 rounded-lg shadow-md z-50 animate-fade-in-out">
      {mensaje}
    </div>
  )}
    </form>
    
  );
}