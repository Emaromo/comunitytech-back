package com.example.java.proyect.share.dto;

import java.io.Serializable;

/**
 * Clase DTO (Data Transfer Object) para transferir datos de usuario
 * entre la capa controlador y la capa servicio.
 * 
 * Implementa Serializable para permitir la conversión del objeto a bytes,
 * por ejemplo para transferencias o almacenamiento.
 */
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // Identificador interno de la base de datos (autogenerado)
    private long id;

    // Identificador público del usuario (UUID)
    private String userId;

    // Nombre del usuario
    private String firstName;

    // Apellido del usuario
    private String lastName;

    // Email del usuario
    private String email;

    // Contraseña en texto plano (solo temporal, no se guarda así)
    private String password;

    // Contraseña encriptada que se almacena en la base de datos
    private String encryptedPassword;

    // Getters y Setters para cada atributo (permiten acceder y modificar los datos)

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
