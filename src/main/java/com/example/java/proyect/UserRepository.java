package com.example.java.proyect;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.java.proyect.entities.UserEntity;

@Repository
public interface  UserRepository extends CrudRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
    
}
/*CrudRepository:
Es una interfaz de Spring Data que proporciona operaciones CRUD básicas
(Create, Read, Update, Delete) sin necesidad de escribir implementación.

UserEntity:es el UserEntity.java
Es una clase que representa la entidad de usuario en la base de datos
(usualmente anotada con @Entity).
Este UserRepository:

Te permite usar métodos CRUD ya listos para la entidad UserEntity.

Además agrega un método personalizado para buscar un usuario por su correo electrónico.

*/