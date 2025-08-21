package com.example.java.proyect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.java.proyect.models.requests.TicketModel;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * ✉️ Servicio responsable de enviar correos electrónicos.
 * [Frontend envía datos nuevos]
         |
         v
[Buscar ticket por ID] --X--> [Excepción si no existe]
         |
         v
[Comparar con valores anteriores y actualizar campos]
         |
         v
[huboCambio?] ----No----> [Log info: no hubo cambios]
         |
        Sí
         |
[Cliente activó notificaciones?] ----No----> [Log info: cliente no quiere notificaciones]
         |
        Sí
         |
[Construir cuerpo HTML con EmailService]
         |
[Enviar email HTML al cliente]
         |
[Guardar ticket actualizado en DB]
         |
         v
[Retornar ticket actualizado]
 * Envía correos en formato HTML para mejor diseño y experiencia.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envía un correo simple de texto plano.
     * @param to destinatario
     * @param subject asunto
     * @param text cuerpo en texto plano
     */
    public void enviarEmail(String to, String subject, String text) {
        // Si querés enviar texto plano podés seguir usando este método
        // (opcional, no usado para actualización en HTML)
    }

    /**
     * Envía un correo en formato HTML.
     * @param to destinatario
     * @param subject asunto
     * @param htmlBody cuerpo HTML
     * @throws MessagingException si hay error en el envío
     */
    public void enviarEmailHtml(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);  // true = es HTML
        helper.setFrom("techresolution24@gmail.com");

        mailSender.send(mensaje);
    }

    /**
     * Construye el cuerpo del email en HTML con el diseño profesional en negro y azul.
     * @param ticket objeto TicketModel con los datos actualizados
     * @return String HTML listo para enviar
     */
    public String construirCuerpoEmail(TicketModel ticket) {
    String colorEstado;
    String mensajeEstado;

    switch (ticket.getEstado().toLowerCase()) {
        case "pendiente":
            colorEstado = "#daba00ff";  // amarillo
            mensajeEstado = "Tu equipo está en espera de revisión.";
            break;
        case "en reparación":
            colorEstado = "#eb6e25ff";  // naranja
            mensajeEstado = "Tu equipo está siendo reparado.";
            break;
        case "listo":
            colorEstado = "#10B981";  // verde
            mensajeEstado = "✅ ¡Tu equipo ya está listo para ser retirado!";
            break;
        default:
            colorEstado = "#374151";  // gris oscuro
            mensajeEstado = "El estado actual de tu ticket es: " + ticket.getEstado();
            break;
    }

    String solucion = ticket.getSolucion() != null ? ticket.getSolucion() : "Aún no disponible";

    // ✅ Mostrar precio solo si el estado es 'listo'
    String precioHtml = "";
    if ("listo".equalsIgnoreCase(ticket.getEstado()) && ticket.getPrecio() != null) {
        precioHtml = "<p><strong>Precio final:</strong> " + String.format("$ %.2f", ticket.getPrecio()) + "</p>";
    }

    String prioridad = ticket.getPrioridad() != null ? ticket.getPrioridad() : "No asignada";

    return "<!DOCTYPE html>" +
            "<html lang='es'>" +
            "<head><meta charset='UTF-8'><title>Actualización de Ticket</title></head>" +
            "<body style='font-family: Arial, sans-serif; background-color: #0F172A; color: #E0E7FF; margin: 0; padding: 20px;'>" +

            "<div style='max-width: 600px; margin: auto; background: #1E293B; border-radius: 10px; padding: 20px;'>" +

            "<h2 style='color: #3B82F6;'>🔔 Actualización de tu ticket #" + ticket.getId() + "</h2>" +

            "<p>Hola,</p>" +

            "<p style='font-size: 16px;'>" + mensajeEstado + "</p>" +

            "<div style='background-color: #111827; padding: 15px; border-radius: 8px; margin-top: 20px;'>" +
            "<p><strong>Estado actual:</strong> " +
            "<span style='color: " + colorEstado + "; font-weight: bold; text-transform: capitalize;'>" + ticket.getEstado() + "</span></p>" +

            "<p><strong>Solución:</strong> " + solucion + "</p>" +

            // ✅ Solo agregar precio si es 'listo'
            precioHtml +

            "</div>" +

            "<p style='margin-top: 30px;'>Si necesitás más información, no dudes en contactarnos.</p>" +

            "<p>Gracias por confiar en <span style='color: #3B82F6;'>Com.Unity Tech - Computers Service</span>.</p>" +

            "<hr style='border-color: #334155; margin: 30px 0;' />" +

            "<p style='font-size: 12px; color: #94A3B8;'>Este correo fue generado automáticamente. Por favor, no respondas a este email.</p>" +

            "</div>" +
            "</body>" +
            "</html>";
}
}