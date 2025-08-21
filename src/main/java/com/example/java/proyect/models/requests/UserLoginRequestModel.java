package com.example.java.proyect.models.requests;
//modelo para el login de un cliente usuario.

public class UserLoginRequestModel {
    
    private String email;
    private String password;
    
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
