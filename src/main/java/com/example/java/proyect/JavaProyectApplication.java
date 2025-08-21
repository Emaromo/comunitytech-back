// Define el paquete donde está esta clase.
// Los paquetes ayudan a organizar el código.
package com.example.java.proyect;

// Importa la clase que permite arrancar una aplicación Spring Boot.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esta anotación indica que esta es una aplicación Spring Boot.
// Activa automáticamente la configuración, el escaneo de componentes y otras funciones básicas.
@SpringBootApplication
public class JavaProyectApplication {

    // Método principal (main), punto de entrada de cualquier aplicación Java.
    public static void main(String[] args) {

        // Este método arranca la aplicación Spring Boot.
        // Carga todas las configuraciones y levanta el servidor web embebido.
        SpringApplication.run(JavaProyectApplication.class, args);

        // Imprime un mensaje en la consola indicando que la app se está ejecutando.
        System.out.println("funcionado");
    }

}
