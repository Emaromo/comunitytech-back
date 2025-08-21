import React from "react";

/**
 * 🔍 SearchBar
 * Barra de búsqueda para filtrar tickets por texto (email, estado, etc.)
 *
 * Props:
 * - value: string actual del input
 * - onChange: función que se dispara al cambiar el texto
 */
export default function SearchBar({ value, onChange }) {
return (
    <div className="w-full md:w-1/2 mb-4">
    <input
        type="text"
        placeholder="Buscar tickets por email, estado, prioridad..."
        className="w-full p-2 rounded-lg bg-gray-900 bg-opacity-90 text-white placeholder-gray-000 border border-blue-800 focus:outline-none focus:ring focus:ring-blue-500"
        value={value}
        onChange={(e) => onChange(e.target.value)}
    />
    </div>
);
}