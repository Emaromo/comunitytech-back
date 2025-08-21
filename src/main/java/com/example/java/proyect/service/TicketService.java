package com.example.java.proyect.service;

import java.time.LocalDate; // Manejar solo la fecha
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.java.proyect.TicketRepository;
import com.example.java.proyect.models.requests.TicketModel;

/**
 * 🎯 Servicio que encapsula la lógica de negocio relacionada a los tickets.
 * - Mantiene la estructura que ya tenías.
 * - Añade métodos para estadísticas y tickets por mes.
 */
@Service
public class TicketService {

    private final TicketRepository ticketRepository; // Acceso a la DB
    private final EmailService emailService;         // Enviar emails

    public TicketService(TicketRepository ticketRepository, EmailService emailService) {
        this.ticketRepository = ticketRepository;
        this.emailService = emailService;
    }

    // ------------------- CRUD básico -------------------

    public TicketModel crearTicket(TicketModel ticket) {
        ticket.setClienteEmail(ticket.getClienteEmail().toLowerCase());
        return ticketRepository.save(ticket);
    }

    public List<TicketModel> listarTodos() {
        return ticketRepository.findAll();
    }

    public List<TicketModel> listarPorCliente(String email) {
        return ticketRepository.findByClienteEmailIgnoreCase(email);
    }

    public Optional<TicketModel> obtenerPorId(Long id) {
        return ticketRepository.findById(id);
    }

    public void eliminarTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    // ------------------- Actualizar ticket -------------------

    public TicketModel actualizarTicket(Long id, TicketModel nuevo) {
        TicketModel ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));

        // Guardamos snapshot de valores anteriores
        String estadoAnterior = ticket.getEstado();
        String solucionAnterior = ticket.getSolucion();
        Double precioAnterior = ticket.getPrecio();
        String prioridadAnterior = ticket.getPrioridad();
        String descripcionAnterior = ticket.getDescripcionProblema();
        Boolean notificarAnterior = ticket.getNotificarCliente();

        boolean huboCambio = false;

        // Actualizamos solo si cambian los valores
        if (nuevo.getEstado() != null && !nuevo.getEstado().equalsIgnoreCase(estadoAnterior)) {
            ticket.setEstado(nuevo.getEstado());

            // Fechas automáticas según estado
            switch (nuevo.getEstado().toLowerCase()) {
                case "pendiente":
                    if (ticket.getFechaPendiente() == null) {
                        ticket.setFechaPendiente(LocalDate.now());
                    }
                    break;
                case "en reparación":
                    if (ticket.getFechaReparacion() == null) {
                        ticket.setFechaReparacion(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")));
                    }
                    break;
                case "listo":
                    if (ticket.getFechaListo() == null) {
                        ticket.setFechaListo(LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires")));
                    }
                    break;
            }
            huboCambio = true;
        }

        if (nuevo.getSolucion() != null && !nuevo.getSolucion().equals(solucionAnterior)) {
            ticket.setSolucion(nuevo.getSolucion());
            huboCambio = true;
        }

        if (nuevo.getPrecio() != null && !nuevo.getPrecio().equals(precioAnterior)) {
            ticket.setPrecio(nuevo.getPrecio());
            huboCambio = true;
        }

        if (nuevo.getPrioridad() != null && !nuevo.getPrioridad().equals(prioridadAnterior)) {
            ticket.setPrioridad(nuevo.getPrioridad());
            huboCambio = true;
        }

        if (nuevo.getDescripcionProblema() != null && !nuevo.getDescripcionProblema().equals(descripcionAnterior)) {
            ticket.setDescripcionProblema(nuevo.getDescripcionProblema());
            huboCambio = true;
        }

        if (nuevo.getNotificarCliente() != null && !nuevo.getNotificarCliente().equals(notificarAnterior)) {
            ticket.setNotificarCliente(nuevo.getNotificarCliente());
            huboCambio = true;
        }

        TicketModel actualizado = ticketRepository.save(ticket);

        // Enviar email si hay cambios y cliente quiere notificaciones
        if (huboCambio && Boolean.TRUE.equals(actualizado.getNotificarCliente())) {
            try {
                String asunto = "🔔 Actualización de tu ticket #" + actualizado.getId();
                String cuerpoHtml = emailService.construirCuerpoEmail(actualizado);
                emailService.enviarEmailHtml(actualizado.getClienteEmail(), asunto, cuerpoHtml);
            } catch (Exception e) {
                System.err.println("⚠️ No se pudo enviar el correo: " + e.getMessage());
            }
        }

        return actualizado;
    }

    // ------------------- Notificaciones -------------------

    public String activarNotificacion(Long id) {
        Optional<TicketModel> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isEmpty()) {
            throw new RuntimeException("❌ Ticket no encontrado");
        }
        TicketModel ticket = ticketOpt.get();
        ticket.setNotificarCliente(true);
        ticketRepository.save(ticket);
        return "✅ Notificaciones activadas para este ticket";
    }

    // ------------------- Estadísticas -------------------

    /**
     * 🔹 Estadísticas generales para las tarjetas
     */
    public Map<String, Long> obtenerEstadisticas() {
        List<TicketModel> tickets = ticketRepository.findAll();

        long total = tickets.size();
        long pendientes = tickets.stream().filter(t -> "pendiente".equalsIgnoreCase(t.getEstado())).count();
        long reparacion = tickets.stream().filter(t -> "en reparación".equalsIgnoreCase(t.getEstado())).count();
        long resueltos = tickets.stream().filter(t -> "resuelto".equalsIgnoreCase(t.getEstado()) || "listo".equalsIgnoreCase(t.getEstado())).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("pendientes", pendientes);
        stats.put("reparacion", reparacion);
        stats.put("resueltos", resueltos);

        return stats;
    }

    /**
     * 🔹 Tickets por mes (para el gráfico)
     */
    public List<Map<String, Object>> obtenerTicketsPorMes() {
    List<TicketModel> tickets = ticketRepository.findAll();
    
    // 🔹 Tomamos el mes actual y creamos 12 meses consecutivos
    int mesActual = LocalDate.now().getMonthValue(); // 1 = Enero, 8 = Agosto
    int yearActual = LocalDate.now().getYear();

    Map<String, Long> meses = new LinkedHashMap<>();

    for (int i = 0; i < 12; i++) {
        // Calculamos mes y año (si supera diciembre, aumentamos año)
        int mes = (mesActual + i - 1) % 12 + 1;
        int year = yearActual + ((mesActual + i - 1) / 12);

        String nombreMes = LocalDate.of(year, mes, 1)
                .getMonth()
                .getDisplayName(TextStyle.SHORT, new Locale("es"));
        meses.put(nombreMes, 0L);
    }

    // 🔹 Contamos tickets por mes (solo del último año o hasta hoy)
    tickets.forEach(t -> {
        LocalDate fecha = t.getFechaCreacion();
        String nombreMes = fecha.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es"));
        if (meses.containsKey(nombreMes)) {
            meses.put(nombreMes, meses.get(nombreMes) + 1);
        }
    });

    // 🔹 Convertimos a lista para React
    List<Map<String, Object>> result = new ArrayList<>();
    for (Map.Entry<String, Long> entry : meses.entrySet()) {
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("mes", entry.getKey());
        mapa.put("tickets", entry.getValue());
        result.add(mapa);
    }

    return result;
}
}