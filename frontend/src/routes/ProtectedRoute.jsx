 //Protege rutas según el rol del token

import React from "react";
import { Navigate } from "react-router-dom";
import { getUserRole } from "../utils/auth";

/**
 * Este componente actúa como una "puerta de seguridad" para rutas específicas.
 * Solo permite acceso si el usuario está logueado y tiene el rol correcto.
 *
 * @param {ReactNode} children - Componente a mostrar si pasa la validación
 * @param {string} roleRequired - Rol necesario (ej: "ROLE_ADMIN")
 */
export default function ProtectedRoute({ children, roleRequired }) {
const userRole = getUserRole();

  // No hay token -> redirigimos al login
if (!userRole) return <Navigate to="/login" replace />;

  // Tiene token pero no tiene el rol adecuado
if (roleRequired && userRole !== roleRequired) {
    return <Navigate to="/login" replace />;
}

  // Todo ok → renderizamos el componente (dashboard)
return children;
}