package com.example.java.proyect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class JavaProyectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaProyectApplication.class, args);
        System.out.println("funcionando");
    }

    // ✅ Bean necesario para encriptar contraseñas con BCrypt
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
