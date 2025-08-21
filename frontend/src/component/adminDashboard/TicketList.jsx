// Componente que permite al admin ver, editar, eliminar tickets, y ver precio y prioridad

import React, { useEffect, useState } from "react";
import api from "../../utils/axiosConfig"; // Axios configurado con token JWT
import SearchBar from "./SearchBar"; // 🔍 Barra de búsqueda integrada
import PrioridadBadge from "./PrioridadBadge"; // 🟢🔴 Badge de prioridad con colores

/**
 * 🎯 Este componente muestra una tabla con todos los tickets registrados en el sistema.
 * - El administrador puede ver cada campo del ticket.
 * - Puede editar el estado, solución, precio y prioridad.
 * - También puede eliminar un ticket.
 * - Se aplica un diseño con Tailwind y scroll horizontal en pantallas pequeñas.
 * - 🔍 Se integró un buscador por email, estado o prioridad.
 * - 🟢🔴 Se agregaron badges visuales según la prioridad.
 */
export default function TicketList() {
  // 🗂 Lista de tickets cargados desde el backend
  const [tickets, setTickets] = useState([]);
  // 💬 Mensajes informativos o de error para el usuario
  const [mensaje, setMensaje] = useState("");
  // ✏️ ID del ticket actualmente en modo edición
  const [editandoId, setEditandoId] = useState(null);
  // 🧾 Campos modificados en modo edición, agrupados por ID
  const [camposEditados, setCamposEditados] = useState({});
  // 🔍 Estado del término de búsqueda
  const [searchTerm, setSearchTerm] = useState("");

  // 🚀 Al montar el componente, cargamos todos los tickets desde el backend
  useEffect(() => {
    const fetchTickets = async () => {
      try {
        const res = await api.get("/tickets");
        if (res.status === 200) {
          setTickets(res.data);
          setMensaje("");
        }
      } catch (error) {
        console.error("❌ Error al obtener tickets:", error);
        setMensaje("❌ No se pudieron cargar los tickets.");
      }
    };
    fetchTickets();
  }, []);

  // 🔄 Actualiza el estado de los campos editables en tiempo real
  const handleChange = (id, campo, valor) => {
    setCamposEditados((prev) => ({
      ...prev,
      [id]: {
        ...prev[id],
        [campo]: valor,
      },
    }));
  };

  // 💾 Envía la actualización de un ticket al backend
  const handleGuardar = async (id) => {
    try {
      const ticketOriginal = tickets.find((t) => t.id === id);
      const cambios = camposEditados[id] || {};

      const datosActualizados = {
        clienteEmail: ticketOriginal.clienteEmail,
        descripcionProblema: ticketOriginal.descripcionProblema,
        estado: cambios.estado ?? ticketOriginal.estado,
        solucion: cambios.solucion ?? ticketOriginal.solucion,
        precio: cambios.precio ?? ticketOriginal.precio,
        prioridad: cambios.prioridad ?? ticketOriginal.prioridad,
        
          notificarCliente: ticketOriginal.notificarCliente ?? true, // ✅ cambio aquí
      };

      // ✅ Corregido: faltaban comillas
      const res = await api.put(`/tickets/${id}`, datosActualizados);

      setTickets((prev) => prev.map((t) => (t.id === id ? res.data : t)));
      setEditandoId(null);
      setCamposEditados((prev) => {
        const nuevos = { ...prev };
        delete nuevos[id];
        return nuevos;
      });
      setMensaje("✅ Ticket actualizado correctamente.");
      setTimeout(() => {
    setMensaje(""); // Limpia el mensaje
  }, 3000);
    } catch (err) {
      console.error("❌ Error al actualizar ticket:", err);
      setMensaje("❌ No se pudo actualizar el ticket.");
    }
  };

  // 🗑️ Elimina un ticket seleccionado
  const handleEliminar = async (id) => {
    try {
      await api.delete(`/tickets/${id}`);
      setTickets(tickets.filter((ticket) => ticket.id !== id));
    } catch (error) {
      console.error("❌ Error al eliminar ticket:", error);
    }
  };

  // 🔍 Filtra los tickets según el término buscado (por email, estado o prioridad)
  //    Si el input contiene una letra, muestra los tickets donde el campo empiece con esa letra
  const filteredTickets = tickets.filter((ticket) => {
    const termino = searchTerm.toLowerCase();
    return (
      ticket.clienteEmail.toLowerCase().startsWith(termino) ||
      ticket.estado.toLowerCase().startsWith(termino) ||
      ticket.prioridad?.toLowerCase().startsWith(termino)
    );
  });

  // 🧾 Renderiza la tabla completa
  return (
    <div className="bg-zinc-00 bg-opacity-90 rounded-xl p-6 ">
      <h2 className="text-2xl font-bold mb-4 text-white text-center">Lista Tickets</h2>
      {mensaje && <p className="mb-4 text-sm text-blue-400">{mensaje}</p>}

      {/* 🔍 Buscador integrado */}
      <div className="mb-8 flex justify-center">
        <SearchBar value={searchTerm} onChange={setSearchTerm} />
      </div>

      <div className="overflow-auto bg-opacity-10 rounded-md border border-gray-900 custom-scrollbar h-[500px]">
        <table className="min-w-full bg-zinc-990 text-white text-sm bg-opacity-90">
          <thead>
            <tr className="bg-zinc800 bg-opacity-10 text-zinc-300 uppercase text-xs">
              <th className="px-4 py-3 text-left">ID</th>
              <th className="px-4 py-3 text-left">Cliente</th>
              <th className="px-4 py-3 text-left">Descripción</th>
              <th className="px-4 py-3 text-left">Estado</th>
              <th className="px-4 py-3 text-left">Solución</th>
              <th className="px-4 py-3 text-left">Precio</th>
              <th className="px-4 py-3 text-left">Prioridad</th>
              <th className="px-4 py-3 text-left">Fecha</th>
              <th className="px-4 py-3 text-left">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {filteredTickets.length === 0 ? (
              <tr>
                <td colSpan="9" className="text-center py-4 text-zinc-400">
                  No hay tickets registrados.
                </td>
              </tr>
            ) : (
              filteredTickets.map((ticket) => {
                const enEdicion = ticket.id === editandoId;
                return (
                  <tr
                    key={ticket.id}
                    className="border-t border-zinc-700 hover:bg-zinc-800 transition"
                  >
                    <td className="px-4 py-2">{ticket.id}</td>
                    <td className="px-4 py-2">{ticket.clienteEmail}</td>
                    <td className="px-4 py-2">{ticket.descripcionProblema}</td>

                    <td className="px-4 py-2">
                      {enEdicion ? (
                        <select
                          value={
                            camposEditados[ticket.id]?.estado || ticket.estado
                          }
                          onChange={(e) =>
                            handleChange(ticket.id, "estado", e.target.value)
                          }
                          className="bg-zinc-700 border border-zinc-500 text-white rounded p-1"
                        >
                          <option value="Pendiente">Pendiente</option>
                          <option value="En reparación">En reparación</option>
                          <option value="Listo">Listo</option>
                        </select>
                      ) : (
                        ticket.estado
                      )}
                    </td>

                    <td className="px-4 py-2">
                      {enEdicion ? (
                        <textarea
                          value={
                            camposEditados[ticket.id]?.solucion ||
                            ticket.solucion ||
                            ""
                          }
                          onChange={(e) =>
                            handleChange(ticket.id, "solucion", e.target.value)
                          }
                          className="bg-zinc-700 border border-zinc-500 text-white rounded p-1 w-full"
                          rows={2}
                        />
                      ) : (
                        ticket.solucion || "-"
                      )}
                    </td>

                    <td className="px-4 py-2">
                      {enEdicion ? (
                        <input
                          type="number"
                          value={
                            camposEditados[ticket.id]?.precio ||
                            ticket.precio ||
                            ""
                          }
                          onChange={(e) =>
                            handleChange(ticket.id, "precio", e.target.value)
                          }
                          className="bg-zinc-700 border border-zinc-500 text-white rounded p-1 w-20"
                        />
                      ) : (
                        `$${ticket.precio || "-"}` // 💲 Precio formateado
                      )}
                    </td>

                    <td className="px-4 py-2">
                      {enEdicion ? (
                        <select
                          value={
                            camposEditados[ticket.id]?.prioridad ||
                            ticket.prioridad ||
                            ""
                          }
                          onChange={(e) =>
                            handleChange(ticket.id, "prioridad", e.target.value)
                          }
                          className="bg-zinc-700 border border-zinc-500 text-white rounded p-1"
                        >
                          <option value="">-</option>
                          <option value="ALTA">Alta</option>
                          <option value="MEDIA">Media</option>
                          <option value="BAJA">Baja</option>
                          <option value="REPARADO">Reparado</option>
                        </select>
                      ) : (
                        <PrioridadBadge prioridad={ticket.prioridad} />
                      )}
                    </td>

                  <td className="px-4 py-2">
                
  {new Date(ticket.fechaCreacion + "T00:00:00").toLocaleDateString("es-AR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  })}

  
</td>

                    <td className="px-4 py-2 flex gap-2 flex-wrap">
                      {enEdicion ? (
                        <button
                          onClick={() => handleGuardar(ticket.id)}
                          className="bg-green-600 hover:bg-green-600 text-white px-2 py-1 rounded text-xs"
                        >
                          💾
                        </button>
                      ) : (
                        <button
                          onClick={() => setEditandoId(ticket.id)}
                          className="bg-blue-600 hover:bg-blue-600 text-white px-2 py-1 rounded text-xs"
                        >
                          ✏️
                        </button>
                      )}
                      <button
                        onClick={() => handleEliminar(ticket.id)}
                        className="bg-red-600 hover:bg-red-600 text-white px-2 py-1 rounded text-xs"
                      >
                        🗑️
                      </button>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
