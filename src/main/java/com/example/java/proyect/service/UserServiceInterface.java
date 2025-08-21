package com.example.java.proyect.service;
import  com.example.java.proyect.share.dto.UserDto;
//Qué es:
//Es un contrato o plantilla que define qué métodos debe tener un servicio, pero no dice cómo se hacen.
//Para qué sirve:
//Define qué hace el servicio, sin entrar en detalles de cómo lo hace.
//Esto permite tener diferentes implementaciones (por ejemplo, para tests, para base de datos real, para mocks).
public interface UserServiceInterface {

    public UserDto createUser(UserDto user);
    
    // Agregá este método para buscar usuario por email
    UserDto getUserByEmail(String email);
}
//Hace que el código sea más flexible y mantenible.

//Permite cambiar la implementación sin afectar a quien use la interfaz.

//Facilita las pruebas (mockear la interfaz).

