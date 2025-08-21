import React, { useState, useEffect } from "react";
import { CheckCircle, Bell, BellOff } from "lucide-react"; // ✅ Iconos de verificación y campanita
import api from "../../utils/axiosConfig"; // o la ruta que uses


/**
 * 🎯 Componente visual que muestra el progreso de un ticket de reparación
 * - Estados del ticket: "pendiente", "en reparación", "listo"
 * - Se representa en formato de pasos, con colores y fechas
 * - Incluye una campanita para que el cliente active notificaciones por email al cambiar el estado
 *
 * 📥 Props que recibe:
 * - estado: string con el estado actual del ticket
 * - fechas: objeto con fechas por cada estado (ej: { pendiente: "2025-07-01", "en reparación": "...", listo: "..." })
 * - clienteEmail: email del cliente (necesario para registrar las notificaciones)
 */
 export default function StateProgress({ estado, fechas = {}, ticketId, clienteEmail, activado, setActivado }) {
  // 🔔 Hook para manejar si las notificaciones están activadas o no
  const [notificacionesActivas, setNotificacionesActivas] = useState(false);

    // ✅ Al montar el componente, verificamos si ya estaban activadas en el backend
  useEffect(() => {
  const obtenerEstadoNotificaciones = async () => {
    try {
      const respuesta = await api.get(`/tickets/cliente/${clienteEmail}`);
      if (respuesta.status === 200) {
        const tickets = respuesta.data;
        const ticket = tickets.find(t => t.id === ticketId);
        if (ticket?.notificarCliente === true) {
          setNotificacionesActivas(true);
          setActivado(true);
        }
      }
    } catch (error) {
      console.error("❌ Error al obtener ticket por email:", error);
    }
  };

  obtenerEstadoNotificaciones();
}, [ticketId, clienteEmail, setActivado]);
  // 🔢 Orden lógico de los pasos del ticket
  const pasos = ["pendiente", "en reparación", "listo"];

  // 🔡 Normalizamos el estado actual a minúsculas
  const estadoActual = estado.toLowerCase();

  // 🔍 Buscamos la posición del estado actual dentro del array de pasos
  const pasoActual = pasos.indexOf(estadoActual);

 

 /**
 * 🔔 Función para activar notificaciones (una sola vez por cliente)
 * Esta función realiza un PUT al backend para activar las notificaciones por email
 * 
 */
const manejarToggleNotificaciones = async () => {
    try {
      const url = `/tickets/${ticketId}/notificacion`; // Endpoint backend
      const respuesta = await api.put(url);

      if (respuesta.status === 200 || respuesta.status === 204) {
        setNotificacionesActivas(true);
        setActivado(true);  // Actualiza estado local
        console.log("📩 Notificaciones activadas para:", clienteEmail);
      } else {
        console.error("❌ Error al activar notificaciones:", respuesta);
      }
    } catch (error) {
      console.error("💥 Error en la solicitud de activación:", error);
    }
  };

  // 🎨 Mapeo de estilos dinámicos por cada estado (colores personalizados)
  const estilos = {
    pendiente: {
      border: "border-yellow-400",
      bg: "bg-yellow-400",
      text: "text-yellow-400",
    },
    "en reparación": {
      border: "border-orange-400",
      bg: "bg-orange-400",
      text: "text-orange-400",
    },
    listo: {
      border: "border-green-400",
      bg: "bg-green-400",
      text: "text-green-400",
    },
  };

  // 🧱 Render del componente
  return (
    <div className="mt-8 p-6 rounded-xl bg-black bg-opacity-90 shadow-md relative">
      
      {/* 🔰 Encabezado con título y botón de notificaciones */}
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-semibold text-white">📦 Estado de Entrega</h2>

        {/* 🔔 Botón de activación de notificaciones por email */}
        <button
          onClick={manejarToggleNotificaciones}
          disabled={notificacionesActivas}
          className=" flex items-center gap-1 px-3 py-1
  bg-gradient-to-r from-black to-black
  border-2 border-blue-900
  rounded-full
  text-sm text-white
  shadow-md shadow-cyan-900/50
  hover:bg-blue-900
  transition"
        >
          {notificacionesActivas ? (
            <>
              <Bell className="w-4 h-4 text-green-400" />
              Notificaciones activadas
            </>
          ) : (
            <>
              <BellOff className="w-4 h-4 text-gray-400" />
              Activar notificaciones
            </>
          )}
        </button>
      </div>

      {/* 🛠️ Línea de progreso con los 3 pasos */}
      <div className="flex justify-between items-center relative">
        {pasos.map((paso, idx) => {
          const completado = idx <= pasoActual; // ✅ Si el paso fue alcanzado
          const clase = estilos[paso]; // 🎨 Estilos del paso actual
          const fechaPaso = fechas[paso] || "—"; // 📅 Fecha asociada al paso, o guión si no existe

          return (
            <div
              key={paso}
              className="flex flex-col items-center text-center w-1/3 z-10"
            >
              {/* 🔘 Círculo del paso, coloreado si ya fue completado */}
              <div
                className={`w-8 h-8 flex items-center justify-center rounded-full 
                ${clase.border} border-2 
                ${completado ? clase.bg : "bg-zinc-800"}`}
              >
                {/* ✅ Icono de tilde si el paso está completado */}
                {completado && <CheckCircle className="w-5 h-5 text-black" />}
              </div>

              {/* 🏷️ Nombre del paso */}
              <p className={`mt-2 font-semibold capitalize ${clase.text}`}>
                {paso}
              </p>

              {/* 📆 Fecha del paso */}
              <p className="text-xs text-gray-300 mt-1">{fechaPaso}</p>
            </div>
          );
        })}

        {/* ➖ Línea horizontal que conecta todos los pasos */}
        <div className="absolute top-4 left-0 w-full h-0.5 bg-zinc-700 z-0" />
      </div>
    </div>
  );
}