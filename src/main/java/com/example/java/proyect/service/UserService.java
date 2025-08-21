//Qué es:
//La clase que implementa la interfaz y tiene la lógica real para realizar las operaciones.

//Para qué sirve:
//Contiene el código que hace las tareas reales, como guardar un usuario en la base de datos, buscar por email, encriptar contraseñas, etc.

//quí está toda la lógica de negocio.

//Puede usar repositorios para acceder a la base de datos.

//Al implementar la interfaz, puede ser reemplazada fácilmente.




// Importaciones necesarias
package com.example.java.proyect.service;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.java.proyect.UserRepository;
import com.example.java.proyect.entities.UserEntity;
import com.example.java.proyect.share.dto.UserDto;

// Anotación que marca esta clase como un servicio de Spring
@Service
public class UserService implements UserServiceInterface, UserDetailsService {

    // Inyección del repositorio que accede a los datos de los usuarios
    @Autowired
    UserRepository userRepository;

    // Inyección del codificador de contraseñas para encriptarlas (usando BCrypt)
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // Método que crea un nuevo usuario en la base de datos
    @Override
    public UserDto createUser(UserDto user) {
        // Crear una nueva entidad para almacenar en la base de datos
        UserEntity userEntity = new UserEntity();

        // Generar un ID único (UUID) para el nuevo usuario y asignarlo al DTO
        user.setUserId(UUID.randomUUID().toString());

        // Copiar datos del DTO (user) a la entidad (userEntity)
        // Esto copia nombre, email, password sin encriptar, etc.
        BeanUtils.copyProperties(user, userEntity);

        // Encriptar la contraseña con BCrypt y asignarla a la entidad
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // Asignar rol fijo por defecto para todo usuario nuevo: "ROLE_CLIENTE"
        // Esto es fundamental para que el campo no quede nulo y se guarde correctamente
        userEntity.setRole("ROLE_CLIENTE");

        // Guardar la entidad en la base de datos y obtener la versión almacenada
        UserEntity storedUserDetails = userRepository.save(userEntity);

        // Crear un nuevo DTO para devolver como respuesta al controlador
        UserDto userToReturn = new UserDto();

        // Copiar datos de la entidad guardada (con id generado, rol asignado, etc) al DTO
        BeanUtils.copyProperties(storedUserDetails, userToReturn);

        // Retornar el DTO con los datos del usuario creado
        return userToReturn;
    }

    /**
     * Método obligatorio para Spring Security, carga un usuario por email para autenticación
     * @param email Email del usuario que intenta loguearse
     * @return UserDetails con email, password y roles (authorities)
     * @throws UsernameNotFoundException si no encuentra el usuario
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar el usuario en la base de datos por email
        UserEntity userEntity = userRepository.findByEmail(email);

        // Si no existe el usuario, lanzar excepción para fallar la autenticación
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        // Si existe, se retorna un objeto User (de Spring Security)
        // con email, contraseña encriptada, y una lista vacía de roles/authorities
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
    @Override
    public UserDto getUserByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email);
    if (userEntity == null) {
        return null;
    }

    UserDto userDto = new UserDto();
    BeanUtils.copyProperties(userEntity, userDto);
    return userDto;
    }

}
