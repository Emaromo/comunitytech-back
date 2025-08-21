package com.example.java.proyect.controllers;

// 🚀 Importaciones necesarias

import java.util.List;           // 📋 Lista de elementos, usada para manejar múltiples tickets
import java.util.Map;       // ❓ Contenedor que puede o no tener un valor (evita null)
import java.util.Optional;       // 🗂 Interfaz para colecciones clave-valor

import org.springframework.beans.factory.annotation.Autowired;           // 🔌 Inyecta dependencias (servicios)
import org.springframework.http.ResponseEntity;                        // 📦 Envolver respuestas HTTP
import org.springframework.web.bind.annotation.DeleteMapping;         // ❌ Mapea peticiones DELETE
import org.springframework.web.bind.annotation.GetMapping;            // 🔎 Mapea peticiones GET
import org.springframework.web.bind.annotation.PathVariable;          // 🏷 Captura variables de la URL
import org.springframework.web.bind.annotation.PostMapping;           // ➕ Mapea peticiones POST
import org.springframework.web.bind.annotation.PutMapping;            // ✏️ Mapea peticiones PUT
import org.springframework.web.bind.annotation.RequestBody;           // 📥 Captura datos JSON enviados en el body
import org.springframework.web.bind.annotation.RequestMapping;        // 📍 Define ruta base del controlador
import org.springframework.web.bind.annotation.RestController;       // ⚡ Indica que es un controlador REST

import com.example.java.proyect.models.requests.TicketModel;          // 🗃 Modelo de ticket (entidad)
import com.example.java.proyect.service.EmailService;                 // 📧 Servicio para enviar emails
import com.example.java.proyect.service.TicketService;
/*
📦 FLUJO DE DATOS PARA OPERACIONES CON TICKETS:Recibe la petición del frontend

Cliente (React, por ejemplo) envía petición (JSON)
    ↓
TicketController
    ↓ Usa TicketService para interactuar con la base de datos
    ↓ Devuelve un TicketModel (convertido automáticamente a JSON)
*/

@RestController // Indica que esta clase es un controlador REST (devuelve JSON)
@RequestMapping("/tickets") // Ruta base para este controlador: http://localhost:8082/tickets

public class TicketController {

    @Autowired
    private TicketService ticketService; // Inyectamos el servicio de tickets

    // ✅ Crear un nuevo ticket
    @PostMapping
    public TicketModel crear(@RequestBody TicketModel ticket) {
        // El cliente envía un JSON con el ticket → se convierte en TicketModel automáticamente
        return ticketService.crearTicket(ticket); // Se guarda y se devuelve el ticket completo
    }

    // 📊 Endpoint para obtener estadísticas de tickets
    @GetMapping("/estadisticas")
    public Map<String, Long> obtenerEstadisticas() {
    return ticketService.obtenerEstadisticas();
    }
    
    //📊 Endpoint para obtener estadísticas de tickes por mes para el grafico
    @GetMapping("/por-mes")
    public List<Map<String, Object>> getTicketsPorMes() {
    return ticketService.obtenerTicketsPorMes();
    }

    // 🔎 Obtener todos los tickets (uso interno o para el administrador)
    @GetMapping
    public List<TicketModel> listarTodos() {
        return ticketService.listarTodos();
    }

    // 🔎 Buscar ticket por ID (para clientes o seguimiento rápido)
    @GetMapping("/{id}")
    public Optional<TicketModel> obtenerPorId(@PathVariable Long id) {
        return ticketService.obtenerPorId(id);
    }

    // 🔎 Buscar todos los tickets por email del cliente (seguimiento de su historial)
    @GetMapping("/cliente/{email}")
    public List<TicketModel> listarPorCliente(@PathVariable String email) {
        return ticketService.listarPorCliente(email);
    }

    // ✏️ Actualizar estado y solución del ticket (solo por administrador)
    @PutMapping("/{id}")
    public TicketModel actualizar(@PathVariable Long id, @RequestBody TicketModel datosActualizados) {
        return ticketService.actualizarTicket(id, datosActualizados);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
    ticketService.eliminarTicket(id);
    return ResponseEntity.ok().build();
}
// ✏️ Actualiza notificaciones por correo
    @PutMapping("/{id}/notificacion")
    public ResponseEntity<String> activarNotificacion(@PathVariable Long id) {
    try {
        String mensaje = ticketService.activarNotificacion(id);
        return ResponseEntity.ok(mensaje);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test-email")
    public String enviarEmailTest() {
        try {
            emailService.enviarEmail(
                "davidvall65@hotmail.com",        // <--- Cambia acá por el email donde quieras recibir el test
                "Prueba de correo desde Spring Boot",
                "Hola! te amo cosita"
            );
            return "✅ Email de prueba enviado correctamente.";
        } catch (Exception e) {
            return "❌ Error enviando email: " + e.getMessage();
        }
    }
}
    
}