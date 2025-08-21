// 🤝 Relación entre los tres archivos principales:
// Clase                  Tipo                 Función                                           Se conecta con
// UserController         Controlador REST     Recibe peticiones HTTP del cliente               Usa UserServiceInterface
// UserServiceInterface   Interfaz             Define lo que debe hacer un servicio de usuario  Implementada por UserService
// UserService            Servicio (@Service)  Lógica para crear usuarios                       Implementa UserServiceInterface


/*
📦 FLUJO DE DATOS EN LA CREACIÓN DE USUARIO:

Cliente (envía JSON con datos del usuario)
    ↓ POST /users
UserController
    ↓ Convierte el JSON en un objeto UserDetailRequestModel
    ↓ Copia los datos a un UserDto (objeto de transferencia de datos)
UserServiceInterface
    ↓ Llama a UserService (la implementación real)
UserService
    ↓ Convierte el DTO en una entidad de base de datos (UserEntity)
    ↓ Guarda el usuario usando UserRepository
    ↓ Devuelve un UserDto con los datos guardados
UserController
    ↓ Copia esos datos a un UserRest (objeto de respuesta)
    ↑ Retorna JSON al cliente

📌 Nota:
- UserDetailRequestModel → clase que recibe datos del cliente.
- UserDto → objeto intermedio usado para mover datos entre capas.
- UserRest → clase que devuelve datos al cliente (sin mostrar campos sensibles como contraseñas).

*/
package com.example.java.proyect.controllers;

// 📦 Importaciones necesarias para controladores, modelos, seguridad, etc.
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // 🔐 Para generar el token JWT
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.java.proyect.UserRepository;
import com.example.java.proyect.config.JWTUtil;
import com.example.java.proyect.entities.UserEntity;
import com.example.java.proyect.models.requests.UserDetailRequestModel;
import com.example.java.proyect.models.requests.UserLoginRequestModel;
import com.example.java.proyect.models.responses.UserRest;
import com.example.java.proyect.service.UserServiceInterface;
import com.example.java.proyect.share.dto.UserDto;

/**
 * 🎯 Controlador REST para operaciones relacionadas a usuarios:
 * - Registro
 * - Login
 * - Test GET
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserServiceInterface userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    /**
     * 🧪 Endpoint de prueba
     */
    @GetMapping
    public String getUser() {
        return "get user details";
    }

    /**
     * ✅ Registro de usuario
     *
     * Recibe los datos del usuario desde el frontend (en formato JSON),
     * los convierte a un DTO, luego se crea el usuario y se responde con
     * un modelo de salida (UserRest) sin la contraseña.
     */
    @PostMapping
    public UserRest createUser(@RequestBody UserDetailRequestModel userDetails) {

        UserRest userToReturn = new UserRest();         // Modelo para la respuesta (sin password)
        UserDto userDto = new UserDto();                // DTO para transferir datos entre capas

        BeanUtils.copyProperties(userDetails, userDto); // Copiamos datos del request al DTO

        UserDto createdUser = userService.createUser(userDto); // Creamos el usuario

        BeanUtils.copyProperties(createdUser, userToReturn);   // Copiamos datos para la respuesta

        return userToReturn;
    }

    /**
     * 🔐 Login de usuario
     *
     * 1. Verifica que el usuario exista.
     * 2. Compara la contraseña enviada con la encriptada en la base.
     * 3. Si es correcta, genera un token JWT con email y rol.
     * 4. Devuelve el token como texto plano (el frontend lo guarda en localStorage).
     */
    // Este endpoint maneja el inicio de sesión de usuarios y devuelve un JWT si las credenciales son válidas.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestModel loginRequest) {
    // 1️⃣ Buscar usuario por email
    UserEntity user = userRepository.findByEmail(loginRequest.getEmail());

    // 2️⃣ Validar existencia y contraseña
    if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getEncryptedPassword())) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales inválidas");
    }

    // 3️⃣ Generar token JWT con email y rol
    String token = JWTUtil.generateToken(user.getEmail(), user.getRole());

    // 4️⃣ También podrías devolver más información (email, rol, token)
    return ResponseEntity.ok().body(token);
}
// llama para client card para sacar el nombre atravez del mail
@GetMapping("/email/{email}")
public ResponseEntity<UserDto> obtenerUsuarioPorEmail(@PathVariable String email) {
    UserDto user = userService.getUserByEmail(email);
    if (user != null) {
        return ResponseEntity.ok(user);
    } else {
        return ResponseEntity.notFound().build();
    }
}

}
