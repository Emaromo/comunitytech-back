package com.example.java.proyect.entities;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Clase entidad que representa la tabla "users" en la base de datos.
 */
@Entity(name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // ID interno de la base de datos (autogenerado)
    @Id
    @GeneratedValue
    private long id;

    // Identificador público del usuario (UUID)
    @Column(nullable = false)
    private String userId;

    // Nombre
    @Column(nullable = false, length = 50)
    private String firstName;

    // Apellido
    @Column(nullable = false, length = 50)
    private String lastName;

    // Email
    @Column(nullable = false, length = 255)
    private String email;

    // Contraseña cifrada
    @Column(nullable = false)
    private String encryptedPassword;

    // Rol del usuario (ej: ROLE_CLIENTE, ROLE_ADMIN)
    @Column(nullable = false)
    private String role = "ROLE_CLIENTE"; // Valor por defecto

    // Getters y Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

/**
 * Clase entidad que representa la tabla "users" en la base de datos.
 *
 * Anotaciones JPA:
 * - @Entity: indica que esta clase es una entidad.
 * - @Id: campo clave primaria.
 * - @GeneratedValue: el valor de id se genera automáticamente.
 * - @Column: define detalles de cada columna (ejemplo: si puede ser nula, longitud).
 *
 * Implementa Serializable para que pueda ser convertida a bytes para almacenamiento o transferencia.
 */

/* 📦 1. Ubicación y propósito
Paquete: com.example.java.proyect.entities

Clase: UserEntity

Función: Representa una entidad de usuario que se mapeará a una tabla llamada users en una base de datos (gracias a @Entity(name = "users")).

🔐 2. Atributos
id: clave primaria, autogenerada (@GeneratedValue). Representa el identificador interno.

userId: identificador público del usuario, probablemente UUID o algo similar.

firstName y lastName: nombre y apellido, con longitud máxima de 50 caracteres.

email: dirección de correo, con longitud máxima de 255.

encryptedPassword: contraseña cifrada (no se guarda la contraseña real).

Todos los campos están anotados con @Column(nullable = false), lo que significa que no pueden ser nulos en la base de datos.

⚙️ 3. Serialización
Implementa Serializable para permitir que los objetos se puedan guardar o transferir, por ejemplo, en sesiones web.

Tiene un serialVersionUID definido correctamente como 1L.

🔄 4. Constructores
Tiene un constructor vacío, esencial para que JPA (Hibernate u otro ORM) pueda instanciar objetos.

Tiene un constructor con todos los campos menos el id, lo que facilita crear instancias del usuario desde servicios o controladores.

✍️ 5. Getters y Setters
Cada campo tiene sus métodos de acceso (get... y set...), y se usa this. para hacer explícita la asignación.

Permiten trabajar con los atributos de manera encapsulada.

✅ 6. Buenas prácticas aplicadas
Uso adecuado de anotaciones JPA (@Entity, @Id, @GeneratedValue, @Column).

Consistencia en el formato del código.

Separación clara entre lógica de persistencia (entidad) y otras capas del sistema.

Listo para integrarse con frameworks como Spring Boot y librerías como Hibernate.

*/
