import React, { useEffect, useState } from "react";
import Footer from "../Footer"; // ⚙️ Footer del sitio
import api from "../../utils/axiosConfig"; // 📡 Axios con token configurado
import { getUserEmail } from "../../utils/localStorage"; // 🔐 Extrae email desde token JWT
import ClientCard from "../adminDashboard/ClientCard"; // 🧾 Tarjeta que muestra el nombre y logout
import StateProgress from "./StateProgress"; // 🚦 Componente visual del estado del ticket
import FAQSection from "./FAQSection"; // ❓ Preguntas frecuentes (nuevo componente)

/**
 * 🎯 ClienteDashboard: panel del cliente autenticado
 * - Muestra los tickets asociados al email
 * - Integra barra de progreso visual
 * - Muestra preguntas frecuentes con animación
 */
export default function ClienteDashboard() {
  const [tickets, setTickets] = useState([]);          // 📬 Lista de tickets del cliente
  const [mensaje, setMensaje] = useState("");          // ⚠️ Mensaje en caso de error o sin tickets
  const [loading, setLoading] = useState(true);        // ⏳ Estado de carga
  const email = getUserEmail();
  const [activado, setActivado] = useState(false);                        // 🔑 Email desde JWT

  /**
   * 🔄 Al montar el componente, trae los tickets del cliente por su email
   */
  useEffect(() => {
    const fetchTickets = async () => {
      try {
        const res = await api.get(`/tickets/cliente/${email}`);
        if (res.status === 200) {
          if (res.data.length > 0) {
            setTickets(res.data);
            setMensaje("");
          } else {
            setMensaje("📭 No tenés tickets creados todavía.");
          }
        }
      } catch (error) {
        console.error("❌ Error al cargar los tickets:", error);
        setMensaje("❌ Hubo un problema al cargar tus tickets.");
      } finally {
        setLoading(false);
      }
    };

    fetchTickets();
  }, [email]);

  return (
    // 🖥️ Contenedor principal con fondo tecnológico
    <div
      className="min-h-screen flex flex-col bg-cover bg-center"
      style={{ backgroundImage: `url('/fondo-tech3.jpg')` }}
    >
      {/* 🧾 Encabezado con nombre del cliente */}
      <header className="w-full px-8 py-6">
        <ClientCard email={email} />
      </header>

      {/* 📦 Contenido central */}
      <main className="flex-grow w-full px-8 flex items-center justify-center">
        <div
          className="w-full max-w-5xl  bg-black bg-opacity-90 rounded-xl p-6 shadow-md
                    hover:shadow-[0_0_20px_rgba(0,102,255,0.4)] transition-shadow duration-300 overflow-auto"
        >
          {/* ⏳ Mensaje mientras se cargan los tickets */}
          {loading ? (
            <p className="text-blue-300 text-lg">⏳ Cargando tus tickets...</p>
          ) : (
            <>
              {/* 🛑 Mostrar mensaje si no hay tickets o hay error */}
              {mensaje && <p className="mb-4 text-gray-300 text-lg">{mensaje}</p>}

              {/* 📋 Tabla si existen tickets */}
              {tickets.length > 0 && (
                <>
                  {/* 🌐 Tabla con scroll y diseño oscuro */}
                  <div className="overflow-x-auto custom-scrollbar max-h-[200px] rounded-md">
                    <table className="min-w-full bg-zinc800 bg-opacity-10  text-white  rounded-md shadow">
                      {/* 🔷 Encabezado con colores */}
                      <thead>
                        <tr className="bg-gradient-to-r from-zinc-900 to-blue-950 text-sm uppercase text-blue-200">
                          <th className="py-3 px-6 text-left">ID</th>
                          <th className="py-3 px-6 text-left">Descripción</th>
                          <th className="py-3 px-6 text-left">Estado</th>
                          <th className="py-3 px-6 text-left">Solución</th>
                          <th className="py-3 px-6 text-left">Precio</th>
                          <th className="py-3 px-6 text-left">Fecha</th>
                        </tr>
                      </thead>

                      {/* 📄 Cuerpo de la tabla */}
                      <tbody>
                        {tickets.map((ticket) => {
                          let estadoColor = "text-white";
                          const estado = ticket.estado?.toLowerCase();

                          if (estado === "pendiente") {
                            estadoColor = "text-yellow-400";
                          } else if (estado === "en reparación") {
                            estadoColor = "text-orange-400";
                          } else if (estado === "listo") {
                            estadoColor = "text-green-400";
                          }

                          return (
                            <tr
                              key={ticket.id}
                              className=" hover:bg-zinc-800 transition"
                              >
                              <td className="py-3 px-6">{ticket.id}</td>
                              <td className="py-3 px-6">{ticket.descripcionProblema}</td>
                              <td className={`py-3 px-6 font-semibold ${estadoColor}`}>
                                {ticket.estado}
                              </td>
                              <td className="py-3 px-6">{ticket.solucion || "-"}</td>
                              <td className="py-3 px-6">
                              <span className="text-green-400 font-semibold">$</span>
                              <span className="text-white ml-1">{ticket.precio}</span>
                              </td>
                              <td className="py-3 px-6">
                              {new Date(ticket.fechaCreacion + "T00:00:00").toLocaleDateString("es-AR", {
                              day: "2-digit",
                              month: "2-digit",
                              year: "numeric",
                              })}
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  </div>

                  {/* 🚦 Progreso de estado visual del primer ticket */}
                  <StateProgress
                  estado={tickets[0].estado}
                  ticketId={tickets[0].id}
                  clienteEmail={email}
                  activado={activado}
                  setActivado={setActivado}
                  fechas={{
                  pendiente: tickets[0].fechaCreacion
                  ? new Date(tickets[0].fechaCreacion+ "T00:00:00").toLocaleDateString("es-AR", {
                  day: "2-digit",
                  month: "2-digit",
                  year: "numeric",
                  })
                  : "—",
                  "en reparación": tickets[0].fechaReparacion
                  ? new Date(tickets[0].fechaReparacion + "T00:00:00").toLocaleDateString("es-AR", {
                  day: "2-digit",
                  month: "2-digit",
                  year: "numeric",
                  })
                  : "—",
                  listo: tickets[0].fechaListo
                  ? new Date(tickets[0].fechaListo + "T00:00:00").toLocaleDateString("es-AR", {
                  day: "2-digit",
                  month: "2-digit",
                  year: "numeric",
                  })
                : "—",
                }}
  
/>


                  {/* ❓ Sección de preguntas frecuentes con animación */}
                  <FAQSection />
                </>
              )}
            </>
          )}
        </div>
      </main>

      {/* ⚙️ Footer fijo del sitio */}
      <Footer />
    </div>
  );}