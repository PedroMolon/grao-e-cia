package com.pedromolon.auth_service.config;

import com.pedromolon.auth_service.domain.Role;
import com.pedromolon.auth_service.domain.User;
import com.pedromolon.auth_service.repository.RoleRepository;
import com.pedromolon.auth_service.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role 'ROLE_ADMIN' not found"));

            String adminEmail = "admin@email.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User adminUser = new User();
                adminUser.setName("Admin");
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(passwordEncoder.encode("admin"));
                adminUser.getRoles().add(adminRole);
                userRepository.save(adminUser);

                System.out.println("===> Admin user created with success! <===");
            } else {
                System.out.println("===> Admin user already exists <===");
            }
        }
    }

}
