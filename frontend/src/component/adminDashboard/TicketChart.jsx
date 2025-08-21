import React, { useState, useEffect } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer,
  Legend,
} from "recharts";
import api from "../../utils/axiosConfig"; // Axios configurado con token si lo usas

// 🧩 Tooltip personalizado con fondo oscuro
const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload && payload.length) {
    return (
      <div className="bg-gray-800 text-white p-2 rounded shadow-lg text-sm">
        <p className="font-semibold">{label}</p>
        <p>{`Tickets: ${payload[0].value}`}</p>
      </div>
    );
  }
  return null;
};

// 🟦 Barra personalizada con efecto hover y sombra
const CustomBar = (props) => {
  const { x, y, width, height, fill } = props;
  const [isHovered, setIsHovered] = useState(false);

  return (
    <g
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      <rect
        x={x}
        y={y}
        width={width}
        height={height}
        rx={4}
        ry={4}
        fill={fill}
        style={{
          filter: isHovered
            ? "drop-shadow(0px 0px 6px rgba(59, 130, 246, 0.8))"
            : "none",
          transition: "all 0.3s ease-in-out",
        }}
      />
    </g>
  );
};

export default function TicketChart() {
  // 🔹 Estado que guarda los datos del gráfico
  const [chartData, setChartData] = useState([]);

  // 🔹 Estado para estadísticas de las tarjetas
  const [stats, setStats] = useState({
    total: 0,
    pendientes: 0,
    reparacion: 0,
    resueltos: 0,
  });

  // 🔹 useEffect para traer los datos del backend
  useEffect(() => {
    const fetchData = async () => {
      try {
        // 1️⃣ Datos por mes para el gráfico
        const resChart = await api.get("/tickets/por-mes");
        setChartData(resChart.data);

        // 2️⃣ Estadísticas generales
        const resStats = await api.get("/tickets/estadisticas");
        setStats(resStats.data);
      } catch (error) {
        console.error("Error al cargar los datos:", error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="p-6 bg-gray-900 rounded-xl shadow-lg text-white space-y-8">
      {/* 🔹 Tarjetas de estadísticas */}
      <div className="grid grid-cols-1 sm:grid-cols-3 md:grid-cols-4 gap-4">
        <div className="bg-black border border-blue-800 text-white text-center rounded-lg px-4 py-2">
          <h4 className="text-sm text-gray-400">Totales</h4>
          <p className="text-2xl font-bold text-white">{stats.total}</p>
        </div>
        <div className="bg-black border border-blue-800 text-white text-center rounded-lg px-4 py-2">
          <h4 className="text-sm text-gray-400">Pendientes</h4>
          <p className="text-2xl font-bold text-yellow-400">{stats.pendientes}</p>
        </div>
        <div className="bg-black border border-blue-800 text-white text-center rounded-lg px-4 py-2">
          <h4 className="text-sm text-gray-400">Reparación</h4>
          <p className="text-2xl font-bold text-orange-400">{stats.reparacion}</p>
        </div>
        <div className="bg-black border border-blue-800 text-white text-center rounded-lg px-4 py-2">
          <h4 className="text-sm text-gray-400">Resueltos</h4>
          <p className="text-2xl font-bold text-green-400">{stats.resueltos}</p>
        </div>
      </div>

      {/* 🔹 Título del gráfico */}
      <div className="text-center mb-6">
  <h2 className="text-2xl font-bold text-white"> Análisis de Tickets Mensuales</h2>
  <p className="text-gray-400 text-sm mt-1">Agosto 2024 – Julio 2025</p>
</div>
      {/* 🔹 Gráfico responsive */}
      <ResponsiveContainer width="100%" height={350}>
        <BarChart
          data={chartData}
          margin={{ top: 20, right: 30, left: 10, bottom: 5 }}
        >
          <CartesianGrid strokeDasharray="3 3" stroke="#003c8fff" />
          <XAxis dataKey="mes" stroke="#e5e7eb" />
          <YAxis stroke="#e5e7eb" domain={[0, 15]}           // rango de 0 a 15 tickets por mes
           ticks={[0, 3, 6, 9, 12, 15]} // marcadores cada 3 tickets
          />
          <Tooltip content={<CustomTooltip />} />
          <Legend wrapperStyle={{ color: "#ffffff" }} />
          <Bar
            dataKey="tickets"
            name="Tickets por mes"
            fill="#2400d7ff"
            stroke="#00040fff"
            barSize={30}
            radius={[10, 10, 0, 0]}
            background={{ fill: "#00000056" }}
            shape={<CustomBar />}
            isAnimationActive={true}
            animationDuration={800}
            barCategoryGap={20}
            barGap={4}
            label={{ position: "top", fill: "#fff", fontSize: 12 }}
          />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}