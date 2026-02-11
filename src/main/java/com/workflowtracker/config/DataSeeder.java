package com.workflowtracker.config;

import com.workflowtracker.entity.Role;
import com.workflowtracker.entity.User;
import com.workflowtracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Check if Manager exists (ID 1)
            if (userRepository.findById(1L).isEmpty()) {
                User manager = User.builder()
                        .name("Manager John")
                        .email("manager@example.com")
                        .password("password") // In real app, this should be encoded
                        .role(Role.MANAGER)
                        .build();
                userRepository.save(manager);
                System.out.println("Seeded Manager User (ID: " + manager.getId() + ")");
            }

            // Check if Employee exists (ID 2)
            if (userRepository.findById(2L).isEmpty()) {
                User employee = User.builder()
                        .name("Employee Jane")
                        .email("employee@example.com")
                        .password("password")
                        .role(Role.EMPLOYEE)
                        .build();
                userRepository.save(employee);
                System.out.println("Seeded Employee User (ID: " + employee.getId() + ")");
            }

            // Note: DB IDs might auto-increment differently if rows were deleted.
            // But since table is likely empty or fresh, they should be 1 and 2.
            // If they are not 1 and 2, the frontend hardcoded IDs might fail.
            // Ideally we'd look up by email, but for this simple demo, this is a start.
        };
    }
}
