package com.example.demo.config;

import com.example.demo.model.Account;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
public class SecurityDataInitializer {

    @Bean
    CommandLineRunner initSecurityData(
            RoleRepository roleRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
                Role role = new Role();
                role.setName("ROLE_ADMIN");
                return roleRepository.save(role);
            });

            Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
                Role role = new Role();
                role.setName("ROLE_USER");
                return roleRepository.save(role);
            });

            // Luôn cập nhật password admin về 123456
            Account admin = accountRepository.findByLoginName("admin").orElseGet(() -> {
                Account newAdmin = new Account();
                newAdmin.setLoginName("admin");
                newAdmin.setRoles(new HashSet<>());
                newAdmin.getRoles().add(adminRole);
                return newAdmin;
            });
            admin.setPassword(passwordEncoder.encode("123456"));
            accountRepository.save(admin);

            if (accountRepository.findByLoginName("user1").isEmpty()) {
                Account user = new Account();
                user.setLoginName("user1");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setRoles(new HashSet<>());
                user.getRoles().add(userRole);
                accountRepository.save(user);
            }
        };
    }
}
