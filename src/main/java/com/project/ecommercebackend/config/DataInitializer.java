package com.project.ecommercebackend.config;

import com.project.ecommercebackend.model.Role;
import com.project.ecommercebackend.model.User;
import com.project.ecommercebackend.model.enums.AppRole;
import com.project.ecommercebackend.repository.RoleRepository;
import com.project.ecommercebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Initializing seed data...");

        Role userRole = findOrCreateRole(AppRole.ROLE_USER);
        Role sellerRole = findOrCreateRole(AppRole.ROLE_SELLER);
        Role adminRole = findOrCreateRole(AppRole.ROLE_ADMIN);

        createUserIfNotExists("user1", "user1@example.com", "password1", Set.of(userRole));
        createUserIfNotExists("seller1", "seller1@example.com", "password2", Set.of(sellerRole));
        createUserIfNotExists("admin", "admin@example.com", "adminPass", Set.of(userRole, sellerRole, adminRole));

        logger.info("Seed data initialization completed.");
    }

    private Role findOrCreateRole(AppRole appRole) {
        return roleRepository.findByRoleName(appRole)
                .orElseGet(() -> {
                    logger.info("Creating role: {}", appRole);
                    return roleRepository.save(new Role(appRole));
                });
    }

    private void createUserIfNotExists(String username, String email, String password, Set<Role> roles) {
        if (userRepository.existsByUserName(username)) {
            userRepository.findByUserName(username).ifPresent(user -> {
                user.setRoles(roles);
                userRepository.save(user);
            });
            return;
        }
        User user = new User(username, email, passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);
        logger.info("Created seed user: {}", username);
    }
}
