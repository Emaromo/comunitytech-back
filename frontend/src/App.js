//Define rutas reales usando React Router

import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import AdminDashboard from "./component/adminDashboard/AdminDashboard";
import ClienteDashboard from "./component/clienteDashboard/ClienteDashboard";
import LoginSignupPage from "./pages/LoginSignupPage";
import ProtectedRoute from "./routes/ProtectedRoute";

/**
 * Como <BrowserRouter> ya está en index.js, acá solo usamos <Routes> y <Route>
 */
function App() {
  return (
    <Routes>
      {/* Página de login y registro */}
      <Route path="/login" element={<LoginSignupPage />} />

      {/* Dashboard para usuarios cliente */}
      <Route
        path="/client"
        element={
          <ProtectedRoute roleRequired="ROLE_CLIENTE">
            <ClienteDashboard />
          </ProtectedRoute>
        }
      />

      {/* Dashboard para administrador */}
      <Route
        path="/admin"
        element={
          <ProtectedRoute roleRequired="ROLE_ADMIN">
            <AdminDashboard />
          </ProtectedRoute>
        }
      />

      {/* Redirección para rutas inexistentes */}
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
}

export default App;