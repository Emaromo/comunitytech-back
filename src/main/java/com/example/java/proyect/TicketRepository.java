package com.example.java.proyect;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.java.proyect.models.requests.TicketModel;

/**
 * Interfaz que extiende JpaRepository y permite operaciones CRUD automáticas.
 * También se puede usar para buscar tickets por email del cliente.
 * Solo harías un método adicional en
 * el repositorio si quisieras contar directamente desde la base de datos (por ejemplo con COUNT o filtros
 * TicketRepository es tu capa de acceso a datos (Data Access Layer).

Es la interfaz que comunica tu aplicación Java con la base de datos.

Al extender JpaRepository<TicketModel, Long> ya te da métodos listos, como:

findAll() → trae todos los tickets

findById(id) → trae un ticket por ID

save(ticket) → guarda o actualiza un ticket

deleteById(id) → elimina un ticket

Es decir, el repositorio no hace lógica de negocio, solo sabe cómo leer o escribir datos en la base de datos.
 * ┌────────────────────┐
│ TicketController   │  << Capa de presentación >>
├────────────────────┤
│ - recibe requests  │
│ - responde HTTP    │
│ - delega a Service │
└───────┬───────────┘
        │
        ▼
┌────────────────────┐
│ TicketService      │  << Capa de lógica de negocio >>
├────────────────────┤
│ - contiene reglas  │
│ - decide qué hacer │
│ - llama al repo    │
└───────┬───────────┘
        │
        ▼
┌────────────────────┐
│ TicketRepository   │  << Capa de acceso a datos >>
├────────────────────┤
│ - extiende JpaRepo │
│ - guarda / busca   │
└────────────────────┘
otro ejemplo

┌──────────────────────────────┐
│       StatCards.jsx (React)  │
│------------------------------│
│ - useEffect() llama a API    │
│ - GET /tickets/estadisticas  │
└────── ┬────────────────┘
        │
        ▼
┌──────────────────────────────┐
│     TicketController.java     │
│------------------------------│
│ - @GetMapping("/estadisticas")│
│ - llama a TicketService       │
│ - devuelve Map<String, Long>  │
└────── ┬────────────────┘
        │
        ▼
┌──────────────────────────────┐
│      TicketService.java       │
│------------------------------│
│ - Llama a TicketRepository    │
│ - ticketRepository.findAll()  │
│ - Filtra y cuenta por estado  │
│ - Crea Map<String, Long>      │
└───────┬────────────────┘
        │
        ▼
┌──────────────────────────────┐
│     TicketRepository.java     │
│------------------------------│
│ - Extiende JpaRepository      │
│ - findAll() devuelve todos    │
│   los TicketModel de MySQL    │
└───────┬──────────────┘
        │
        ▼
┌──────────────────────────────┐
│         Base de Datos         │
│------------------------------│
│ - Tabla tickets               │
│ - Contiene estado, email, etc │
└──────────────────────────────┘
 */
@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Long> {

    // Busca todos los tickets asociados a un email de cliente
    //List<TicketModel> findByClienteEmail(String clienteEmail);
List<TicketModel> findByClienteEmailIgnoreCase(String clienteEmail);
}


