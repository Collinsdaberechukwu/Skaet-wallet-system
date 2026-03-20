package com.collins.Skaet_wallet_system.util;

import com.collins.Skaet_wallet_system.enums.RoleType;
import com.collins.Skaet_wallet_system.model.Role;
import com.collins.Skaet_wallet_system.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (roleRepository.findByRoleName(RoleType.USER) == null) {

            Role userRole = new Role();
            userRole.setRoleName(RoleType.USER);
            userRole.setDescription("Default role for regular users");

            roleRepository.save(userRole);
        }
    }
}
