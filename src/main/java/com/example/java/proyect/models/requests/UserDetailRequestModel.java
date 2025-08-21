// Paquete donde se encuentra esta clase, siguiendo la estructura del proyecto.
package com.example.java.proyect.models.requests;

/*
 * Esta clase representa el modelo de datos que se va a recibir
 * cuando se envíe un JSON desde Postman o desde un formulario en el frontend.
 * Es decir, mapea el cuerpo (body) de la solicitud HTTP a un objeto Java.
 */
public class UserDetailRequestModel {

    // Atributos privados que representan los campos del JSON
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    // Getter: permite obtener el valor de firstName
    public String getFirstName() {
        return this.firstName;
    }

    // Setter: permite modificar el valor de firstName
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter para lastName
    public String getLastName() {
        return this.lastName;
    }

    // Setter para lastName
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter para email
    public String getEmail() {
        return this.email;
    }

    // Setter para email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter para password
    public String getPassword() {
        return this.password;
    }

    // Setter para password
    public void setPassword(String password) {
        this.password = password;
    }
}
