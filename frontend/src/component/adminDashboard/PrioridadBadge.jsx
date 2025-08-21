import React from "react";

/**
 * 🔴🟠🟡 PrioridadBadge
 * Muestra una etiqueta de prioridad con color según el valor:
 * - alta → rojo
 * - media → naranja
 * - baja → amarillo
 *
 * Props:
 * - prioridad: string ('alta', 'media', 'baja')
 */
export default function PrioridadBadge({ prioridad }) {
let bgColor = "";
let textColor = "text-white";

switch (prioridad?.toLowerCase()) {
    case "alta":
    bgColor = "bg-red-600";
    break;
    case "media":
    bgColor = "bg-orange-500";
    break;
    case "baja":
    bgColor = "bg-yellow-400 text-black";
    break;
    default:
    bgColor = "bg-green-500";
}

return (
    <span
    className={`inline-block px-3 py-1 rounded text-sm font-semibold shadow-md ${bgColor} ${textColor}`}
    >
    {prioridad}
    </span>
);
}