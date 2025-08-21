import React from "react";
import TicketForm from "./TicketForm";      // 🧾 Formulario para crear tickets
import TicketList from "./TicketList";      // 📋 Lista de todos los tickets
import TechnicalCard from "./TechnicalCard";
import TicketChart from "./TicketChart";    // 📈 Gráfico mensual de tickets
import Footer from "../Footer";               // ⚙️ Footer personalizado importado

/**
 * 🎯 Dashboard principal para el administrador.
 * Layout para escritorio con:
 * - Barra superior con bienvenida
 * - Sección principal con 3 columnas (Crear ticket, Lista, Gráfico)
 * - Lista centrada verticalmente aunque quede un poco diferente
 * - Footer importado personalizado al final
 */
export default function AdminDashboard() {
  return (
    // Contenedor principal con fondo tech
    <div
      className="min-h-screen flex flex-col bg-cover bg-center"
      style={{ backgroundImage: `url('/fondo-tech3.jpg')` }}
    >
      {/* Header superior con bienvenida y logout */}
      <header className="w-full px-8 py-6">
        <TechnicalCard />
      </header>

      {/* Contenedor principal flex-grow para ocupar espacio disponible */}
      <main className="flex-grow w-full px-8 flex items-center justify-center">
        {/* Grid principal con 12 columnas y espacio entre columnas */}
        <div
          className="grid grid-cols-12 gap-8"
          style={{ height: "calc(80vh)", maxHeight: "800px" }}
        >
          {/* Columna 1: Crear nuevo ticket */}
          <section
            className="col-span-3 bg-zinc-900 bg-opacity-60 rounded-xl p-6 shadow-md
                       hover:shadow-[0_0_20px_rgba(0,102,255,0.4)]
                       transition-shadow duration-300 overflow-auto max-h-full"
          >
            <TicketForm />
          </section>

          {/* Columna 2: Lista de tickets - centrado verticalmente */}
          <section
            className="col-span-6 flex items-center bg-zinc-900 bg-opacity-60 rounded-xl p-6 shadow-md
                       hover:shadow-[0_0_20px_rgba(0,102,255,0.4)]
            duration-300 overflow-y-auto max-h-full"
          >
            {/* Wrapper para permitir scroll interno y contenido centrado */}
            <div className="w-full">
              <TicketList />
            </div>
          </section>

          {/* Columna 3: Gráfico tickets por mes */}
          <section
            className="col-span-3 bg-zinc-900 bg-opacity-60 rounded-xl p-6 shadow-md
                       hover:shadow-[0_0_20px_rgba(0,102,255,0.4)]
                       transition-shadow duration-300 overflow-auto max-h-full"
          >
            <TicketChart />
          </section>
        </div>
      </main>

      {/* Footer personalizado importado */}
      <Footer />
    </div>
  );
}