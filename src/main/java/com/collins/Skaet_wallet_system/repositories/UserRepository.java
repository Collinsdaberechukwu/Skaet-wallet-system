package com.collins.Skaet_wallet_system.repositories;

import com.collins.Skaet_wallet_system.model.ApplicationUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser,Long> {
    ApplicationUser findByEmail(String username);

    boolean existsByEmail(@NotBlank(message = "User email should not be null or blank") @Email(message = "email is required") String email);
}
