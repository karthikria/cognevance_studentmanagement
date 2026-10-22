
package com.studentmanagement.config;

import com.studentmanagement.entity.AppUser;
import com.studentmanagement.enums.Role;
import com.studentmanagement.repository.AppUserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeAdmin(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.initial-admin.email:s.karthikria@gmail.com}")
            String adminEmail,
            @Value("${app.initial-admin.password:Admin@12345}")
            String adminPassword) {

        return args -> {


    AppUser admin = appUserRepository
            .findByEmailIgnoreCase(adminEmail)
            .orElseGet(AppUser::new);

    admin.setEmail(adminEmail);
    admin.setPasswordHash(
            passwordEncoder.encode(adminPassword)
    );
    admin.setRole(Role.ADMIN);
    admin.setEnabled(true);

    appUserRepository.save(admin);

    System.out.println("Admin credentials refreshed: " + adminEmail);


        };
    }
}
