package com.example.java.proyect;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.java.proyect.entities.UserEntity;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "davidvall65@hotmail.com";

            // Verificar si ya existe
            if (userRepository.findByEmail(adminEmail) == null) {
                UserEntity admin = new UserEntity();
                admin.setUserId(UUID.randomUUID().toString());
                admin.setFirstName("David");
                admin.setLastName("Val");
                admin.setEmail(adminEmail);
                admin.setEncryptedPassword(passwordEncoder.encode("41961863David"));
                admin.setRole("ROLE_ADMIN");

                userRepository.save(admin);
                System.out.println("✅ Usuario administrador creado automáticamente");
            } else {
                System.out.println("ℹ️ El usuario administrador ya existe");
            }
        };
    }
}
