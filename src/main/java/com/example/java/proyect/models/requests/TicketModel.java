// 📦 Paquete donde vive esta clase. Acá simplemente está organizado dentro de "models.requests"
package com.example.java.proyect.models.requests;

// 🕓 Librerías para trabajar con fechas
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Entity;              // 👉 Marca la clase como entidad de base de datos
import jakarta.persistence.GeneratedValue;     // 👉 Define que el valor del ID será generado automáticamente
import jakarta.persistence.GenerationType;     // 👉 Especifica la estrategia de generación del ID
import jakarta.persistence.Id;                 // 👉 Marca el campo que será la clave primaria
import jakarta.persistence.PrePersist;         // 👉 Método que se ejecuta antes de insertar en base de datos

/**
 * 🎯 Clase que representa un ticket de reparación.
 * Esta clase se guarda como una tabla en la base de datos gracias a la anotación @Entity.
 */
@Entity  // 🏷️ Le dice a Spring y JPA que esta clase es una entidad persistente (una tabla en la base)
public class TicketModel {

    // ------------------- Atributos -------------------

    // 🔑 ID único del ticket, es la clave primaria de la tabla y se genera automáticamente
    @Id  // 📌 Indica que este campo es la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 🔁 Estrategia IDENTITY: deja que la base de datos genere el ID (ej: AUTO_INCREMENT)
    private Long id;

    // 📧 Email del cliente que generó el ticket
    private String clienteEmail;

    // 📝 Descripción del problema del dispositivo
    private String descripcionProblema;

    // 🔄 Estado actual del ticket (pendiente, en reparación, listo, etc.)
    private String estado;

    // ✅ Solución técnica aplicada
    private String solucion;

    // 📅 Fecha automática en que se creó el ticket (se setea sola al insertar)
    private LocalDate fechaCreacion;

    // 💰 Costo estimado o real del arreglo
    private Double precio;

    // ⚠️ Prioridad (Alta, Media, Baja, Reparado, etc.)
    private String prioridad;

    // 🔔 Nuevo campo para activar/desactivar notificaciones
    private Boolean notificarCliente = false;

    // 🕓 Fecha en que el ticket fue marcado como "Pendiente"
    private LocalDate fechaPendiente;

    // 🛠️ Fecha en que el ticket fue marcado como "En reparación"
    private LocalDate fechaReparacion;



    // 📦 Fecha en que el ticket fue marcado como "Listo"
    private LocalDate fechaListo;

    

    // ------------------- Lógica para asignar fecha automáticamente -------------------

    /**
     * ⚙️ Este método se ejecuta automáticamente ANTES de guardar el ticket por primera vez.
     * Si no se setea una fecha, le pone la fecha actual usando la zona horaria de Argentina.
     */
    @PrePersist
protected void onCreate() {
    // Si el admin ingresó fechaPendiente, usamos esa fecha como fechaCreacion también
    if (this.fechaCreacion == null) {
        this.fechaCreacion = this.fechaPendiente != null ? this.fechaPendiente : ZonedDateTime
            .now(ZoneId.of("America/Argentina/Buenos_Aires"))
            .toLocalDate();
    }
        
    }

    // ------------------- Getters y Setters -------------------
    // Estos métodos permiten acceder y modificar los campos privados de la clase

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    public Boolean getNotificarCliente() {
    return notificarCliente;
}

public void setNotificarCliente(Boolean notificarCliente) {
    this.notificarCliente = notificarCliente;
}

    public LocalDate getFechaPendiente() {
        return fechaPendiente;
    }

    
    public void setFechaPendiente(LocalDate fechaPendiente) {
        this.fechaPendiente = fechaPendiente;
    }

    public LocalDate getFechaReparacion() {
        return fechaReparacion;
    }

    public void setFechaReparacion(LocalDate fechaReparacion) {
        this.fechaReparacion = fechaReparacion;
    }

    public LocalDate getFechaListo() {
        return fechaListo;
    }

    public void setFechaListo(LocalDate fechaListo) {
        this.fechaListo = fechaListo;
    }
}