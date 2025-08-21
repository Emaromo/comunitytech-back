import React, { useState } from "react";
import { ChevronDown, ChevronUp } from "lucide-react"; // Íconos de despliegue

/**
 * 🧠 Componente de Preguntas Frecuentes (FAQ)
 * Estilo moderno, negro y azul, con preguntas desplegables.
 */
const FAQSection = () => {
  // 📌 Lista de preguntas y respuestas
  const preguntas = [
    {
      pregunta: "¿Cuándo recibiré mi equipo reparado?",
      respuesta:
        "Recibirás tu equipo dentro de los 3 a 5 días hábiles, dependiendo de la complejidad del problema.",
    },
    {
      pregunta: "¿Dónde puedo consultar el estado de mi reparación?",
      respuesta:
        "Desde este panel podés ver en qué etapa está tu ticket: pendiente, en reparación o listo.",
    },
    {
      pregunta: "¿Qué pasa si no estoy conforme con la reparación?",
      respuesta:
        "Podés contactarnos dentro de los 7 días para realizar un reclamo o revisión adicional sin costo.",
    },
    {
      pregunta: "¿Cómo me notifican los avances?",
      respuesta:
        "Te enviamos actualizaciones por email. También podés revisar este panel con tu usuario.",
    },
    {
      pregunta: "¿Cuáles son los métodos de pago?",
      respuesta:
        "Podés pagar en efectivo, transferencia o con tarjeta al momento de retirar el equipo.",
    },
  ];

  // 🔽 Índice de la pregunta actualmente abierta
  const [activa, setActiva] = useState(null);

  return (
    <div className="  p-6 rounded-xl ">
      <h2 className="text-xl text-white mb-4 font-semibold ">
        ❓ Preguntas Frecuentes
      </h2>

      <ul>
        {preguntas.map((item, index) => (
          <li
            key={index}
            className="mb-4 border-2 border-blue-900 rounded-lg bg-gradient-to-b from-black to-blue-000 border-blue-900 )"
          >
            {/* 🔘 Pregunta (clickeable para expandir) */}
            <button
              onClick={() => setActiva(activa === index ? null : index)}
              className="w-full flex justify-between items-center px-4 py-3 text-left text-white hover:bg-zinc-900 "
            >
              <span>{item.pregunta}</span>
              {activa === index ? (
                <ChevronUp className="w-5 h-5 text-blue-400" />
              ) : (
                <ChevronDown className="w-5 h-5 text-blue-400" />
              )}
            </button>

            {/* 🔽 Respuesta (solo si está activa) */}
            {activa === index && (
              <div className="px-4 pb-4 text-blue-200 ">
                {item.respuesta}
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default FAQSection;