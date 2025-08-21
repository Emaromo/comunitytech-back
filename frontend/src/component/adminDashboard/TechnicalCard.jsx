import React from "react";
import { UserCog, LogOut } from "lucide-react";
import { logout } from "../../utils/auth"; // 🔐 Función para cerrar sesión

/**
 * Barra horizontal con mensaje de bienvenida y botón de logout.
 */
export default function TechnicalCard() {
  return (
    <div className="w-full bg-black border border-blue-700 rounded-lg px-6 py-4 flex items-center justify-between transition duration-300 hover:ring-2 hover:ring-blue-500 hover:shadow-blue-500/30 shadow">
      {/* Sección izquierda: Icono + Bienvenida */}
      <div className="flex items-center gap-3">
        <div className="p-2 bg-zinc-800 rounded-full">
          <UserCog className="w-6 h-6 text-blue-400" />
        </div>
        <h2 className="text-white text-lg font-bold">
          Bienvenido,David Vall
        </h2>
      </div>

      {/* Sección derecha: Botón Logout */}
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